package me.iatog.characterdialogue.dialogs.method.conditional;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class ConditionalMethod extends DialogMethod<CharacterDialoguePlugin> {

	public ConditionalMethod(CharacterDialoguePlugin main) {
		super("conditional", main);
		/**
		 * PREDESIGN:
		 * lines:
		 * - send: hello
		 * - conditional:        CONDITIONAL    |               CONDITIONAL = TRUE                           |  CONDITIONAL = FALSE
		 * - conditional: %player_name% == aatog | (RUN_DIALOGUE/STOP_AND_SEND_MESSAGE/STOP/RUN_METHOD): %arg% | (METHODS)
		 * - conditional: %time% > 5000 | STOP_AND_SEND_MESSAGE: %npc_name%: It would be better to talk about it later
		 * <p>
		 * NEW
		 * conditional{condition="%player_name% == aatog", ifTrue="RUN_DIALOGUE: dialogue", ifFalse="STOP_SEND_MSG: &c[NPC] &b%npc_name%&f: Who are you?"}
		 */
	}

	@Override
	public void execute(MethodContext context) {
		MethodConfiguration configuration = context.getConfiguration();
		DialogSession session = context.getSession();
		try {
			String condition = configuration.getString("condition", "");
			String ifTrue = configuration.getString("ifTrue", "");
			String ifFalse = configuration.getString("ifFalse", "");
			session.sendDebugMessage("Condition: " + condition, "ConditionalMethod");
			session.sendDebugMessage("ifTrue: " + ifTrue, "ConditionalMethod");
			session.sendDebugMessage("ifFalse: " + ifFalse, "ConditionalMethod");

			if(condition.isEmpty() || ifTrue.isEmpty() || ifFalse.isEmpty()) {
				getProvider().getLogger().severe("The dialogue '" + session.getDialogue().getName() + "' has an invalid configuration in L" + session.getCurrentIndex());
				getProvider().getLogger().severe("Condition: " + found(!condition.isEmpty()));
				getProvider().getLogger().severe("ifTrue: " + found(!ifTrue.isEmpty()));
				getProvider().getLogger().severe("ifFalse: " + found(!ifFalse.isEmpty()));
				this.destroy(context);
				session.sendDebugMessage("Error obtaining configuration", "ConditionalMethod");
				return;
			}

			boolean conditionResult;

			try {
				conditionResult = evaluateCondition(context.getPlayer(), condition);
			} catch(IllegalArgumentException e) {
				context.getPlayer().sendMessage(TextUtils.colorize("&c&lFatal error occurred."));
				getProvider().getLogger().warning("The dialogue '" + session.getDialogue().getName() + "' has an invalid condition in L" + session.getCurrentIndex());

				this.destroy(context);
				return;
			}

			String expression = (conditionResult ? ifTrue : ifFalse).replace("%apostrophe%", "'");
			String method = expression.trim().toUpperCase();
			String argument = "";

			session.sendDebugMessage("&a" + condition + "&7: &c" + conditionResult, "ConditionalMethod");

			if(expression.contains(":")) {
				String[] parts = expression.split(":", 2);
				method = parts[0].trim().toUpperCase();
				argument = parts.length > 1 ? parts[1].trim() : "";
			}

			ConditionalExpression conditionalExpression = ConditionalExpression.valueOf(method);
			conditionalExpression.execute(new ConditionData(session, getProvider(), argument), context.getConsumer());
		} catch(IndexOutOfBoundsException|IllegalArgumentException e) {
			context.getPlayer().sendMessage(TextUtils.colorize("&c&lFatal error occurred."));
			getProvider().getLogger().warning("The dialogue '" + session.getDialogue().getName() + "' has an invalid format in L" + session.getCurrentIndex());
			e.printStackTrace();
			this.destroy(context);
		}
	}

	private String found(boolean bool) {
		return bool ? "Found" : "Not found";
	}

	public boolean evaluateCondition(Player player, String condition) {
		Operator operator = null;
		String leftSide = null;
		String rightSide = null;

		for (Operator op : Operator.values()) {
			if (condition.contains(op.getText())) {
				operator = op;
				String[] parts = condition.split(Pattern.quote(op.getText()));
				leftSide = Placeholders.translate(player, parts[0].trim());
				rightSide = Placeholders.translate(player, parts[1].trim());
				break;
			}
		}

		if (operator == null || leftSide == null || rightSide == null) {
			throw new IllegalArgumentException("Invalid format");
		}

		return compareCondition(leftSide, rightSide, operator);
	}

	private boolean compareCondition(String left, String right, Operator operator) {
		try {
			double leftDouble = 0;
			double rightDouble = 0;

			if(isDouble(left))
				leftDouble = Double.parseDouble(left);

			if(isDouble(right))
				rightDouble = Double.parseDouble(right);

			switch (operator) {
				case LESS_THAN:
					return leftDouble < rightDouble;
                case LESS_THAN_EQUAL:
					return leftDouble <= rightDouble;
                case MORE_THAN:
					return leftDouble > rightDouble;
                case MORE_THAN_EQUAL:
					return leftDouble >= rightDouble;
                case EQUAL_TO:
					return left.equals(right);
				case NOT_EQUAL_TO:
					return !left.equals(right);
				default:
					return false;
            }
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException("The values cannot be compared: " + left + " & " + right);
		}
	}

	public boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
}
