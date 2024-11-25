package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.session.EmptyDialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ConditionalMethod extends DialogMethod<CharacterDialoguePlugin> {

	public ConditionalMethod(CharacterDialoguePlugin main) {
		super("conditional", main);
		/**
		 * PREDESIGN:
		 * lines:
		 * - send: hello
		 * - conditional:        CONDITIONAL    |               CONDITIONAL = TRUE                           |  CONDITIONAL = FALSE
		 * - conditional: %playername% == aatog | (RUN_DIALOGUE/STOP_AND_SEND_MESSAGE/STOP/RUN_METHOD): %arg% | (METHODS)
		 * - conditional: %time% > 5000 | STOP_AND_SEND_MESSAGE: %npc_name%: It would be better to talk about it later
		 * - conditional: %kills% == 20 |
		 */
	}

	@Override
	public void execute(Player player, String arg, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		try {
			String[] arguments = arg.split("\\|");
			String condition = arguments[0].trim();
			String ifTrue = arguments[1].trim();
			String ifFalse = arguments[2].trim();

			boolean conditionResult = false;

			try {
				conditionResult = evaluateCondition(player, condition);
			} catch(IllegalArgumentException e) {
				player.sendMessage(TextUtils.colorize("&c&lFatal error occurred."));
				getProvider().getLogger().warning("The dialogue '" + session.getDialogue().getName() + "' has an invalid condition in L" + session.getCurrentIndex());
				session.destroy();
				return;
			}

			String actualExpression = conditionResult ? ifTrue : ifFalse;
			String method;
			String argument;

			// DEBUG soon
			//player.sendMessage(TextUtils.colorize("&e" + condition + "&7: &c" + conditionResult));

			if(actualExpression.contains(":")) {
				String[] parts = actualExpression.split(":", 2);
				method = parts[0].trim().toUpperCase();
				argument = parts.length > 1 ? parts[1].trim() : "";
			} else {
				method = actualExpression.trim().toUpperCase();
				argument = "";
			}

			ConditionalExpression expression = ConditionalExpression.valueOf(method);
			expression.execute(new ConditionData(session, getProvider(), argument), completed);
			// pero estas chill de cojones
		} catch(IndexOutOfBoundsException|IllegalArgumentException e) {
			player.sendMessage(TextUtils.colorize("&c&lFatal error occurred."));
			getProvider().getLogger().warning("The dialogue '" + session.getDialogue().getName() + "' has an invalid format in L" + session.getCurrentIndex());
			e.printStackTrace();
			completed.accept(CompletedType.DESTROY);
		}
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

	public enum Operator {
		LESS_THAN_EQUAL("<="),
		MORE_THAN_EQUAL(">="),
		NOT_EQUAL_TO("!="),
		LESS_THAN("<"),
		MORE_THAN(">"),
		EQUAL_TO("==");


		private final String text;

		Operator(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}
	}

	public enum ConditionalExpression {
		//RUN_DIALOGUE/STOP_AND_SEND_MESSAGE/STOP/RUN_METHOD/CONTINUE
		RUN_DIALOGUE((data, completed) -> {
			DialogSession session = data.getSession();
			Player player = session.getPlayer();
			String expression = data.getExpression();
			//session.destroy();
			completed.accept(CompletedType.DESTROY);

			if(!data.getMain().getCache().getDialogues().containsKey(expression) || expression.isEmpty()) {
				data.getMain().getLogger().severe("The dialogue '" + expression + "' was not found.");
				player.sendMessage(TextUtils.colorize("&c&lUnknown dialogue found."));
				return;
			}

			data.getMain().getApi().runDialogue(player, expression, false);
		}),
		STOP_SEND_MSG((data, completed) -> {
			Player player = data.getSession().getPlayer();
			completed.accept(CompletedType.DESTROY);
			player.sendMessage(Placeholders.translate(player, data.getExpression()));
		}),
		STOP((data, completed) -> {
			completed.accept(CompletedType.DESTROY);
		}),
		RUN_METHOD((data, completed) -> {
			//completed.accept(CompletedType.PAUSE);
			DialogSession session = data.getSession();
			Player player = session.getPlayer();
			String expression = data.getExpression();

			if (data.getExpression().isEmpty() || data.getMain().getCache().getMethods().containsKey(expression)) {
				session.destroy();
				data.getMain().getLogger().severe("The dialogue '" + expression + "' was not found.");
				player.sendMessage("&c&lUnknown method found.");
				return;
			}

			data.getMain().getApi().runDialogueExpression(player, expression, session.getDisplayName(), SingleUseConsumer.create(completedRes -> {
				//player.sendMessage(TextUtils.colorize("&c&lUnknown method found"));
				//session.destroy();
				//data.getMain().getLogger().severe("The method '" + expression + "' was not found.");
				completed.accept(CompletedType.CONTINUE);
			}), new EmptyDialogSession(data.getMain(), player, Collections.singletonList(expression), session.getDisplayName()));

		}),
		CONTINUE((data, completed) -> {
			completed.accept(CompletedType.CONTINUE);
		});

		private final BiConsumer<ConditionData, Consumer<CompletedType>> action;

		ConditionalExpression(BiConsumer<ConditionData, Consumer<CompletedType>> action) {
			this.action = action;
		}

		public void execute(ConditionData data, Consumer<CompletedType> completed) {
			action.accept(data, completed);
		}
	}

	public static class ConditionData {

		private final DialogSession session;
		private final CharacterDialoguePlugin main;
		private final String expression;

		public ConditionData(DialogSession session, CharacterDialoguePlugin main, String expression) {
			this.session = session;
			this.main = main;
			this.expression = expression;
		}

		public DialogSession getSession() {
			return session;
		}

		public String getExpression() {
			return expression;
		}

		public CharacterDialoguePlugin getMain() {
			return main;
		}
	}
}
