package client.command;

import client.forms.HumanBeingForm;
import client.handlers.SessionHandler;
import client.netw.UDP;
import client.util.console.Cons;
import common.build.response.NotLoggedInRes;
import common.exceptions.*;
import common.build.request.*;
import common.build.response.*;

import java.io.IOException;

/**
 * The type Add.
 */
public class Add extends Command {
    private final Cons console;
    private final UDP client;

    public Add(Cons console, UDP client) {
        super("add {element}");
        this.console = console;
        this.client = client;
    }

    @Override
    public boolean apply(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) throw new WrongAmountOfElements();
            console.println("* Создаем убийцу:");

            var newPerson = (new HumanBeingForm(console)).build();
            var response = client.sendAndReceiveCommand(new AddReq(newPerson, SessionHandler.getCurrentUser()));
            if (response.getError() != null && !response.getError().isEmpty()) {
                throw new API(response.getError());
            }

            if (response.getClass().equals(NotLoggedInRes.class)) {
                console.printError("Вы не залогинены, войдите");
            }
            if (response.getClass().equals(NoSuchCommandRes.class)) {
                console.printError("??? дурачок залогинься");
            }
            if (response.getClass().equals(getTargetClassCastOrErrorResponse(this.getClass()))) {
                console.println("Новый drug с id=" + ((AddRes) response).newId + " успешно добавлен!");

                return true;
            }

        } catch (WrongAmountOfElements exception) {
            console.printError("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
        } catch (InvalidForm exception) {
            console.printError("Поля друга не валидны! Продукт не создан!");
        } catch (IOException e) {
            console.printError("Ошибка взаимодействия с сервером");
        } catch (API e) {
            console.printError(e.getMessage());
        } catch (IncorrectInputInScript ignored) {
        }
        return false;
    }
}