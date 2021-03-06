package main;

import controllers.MainController;

public class App {
    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.displayMenu();
    }
}
