package pl.balwinski.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Getter
@Setter
public class UiPage {

    private List<Option> options;
    private String name;

    public UiPage(List<Option> options, String name) {
        this.name = name;
        this.options = options;
    }

    public UiPage() {
    }

    public void runPage() {
        Optional<Option> optionOptional = Optional.empty();
        while (optionOptional.isEmpty()) {
            showPrompt();
            String input = getUserSelection();
            optionOptional = options.stream().filter(o -> o.selector().equals(input)).findFirst();
            if (optionOptional.isEmpty()) {
                System.out.println("Invalid option!");
                continue;
            }
            Option option = optionOptional.get();

            //debug
            System.out.println("Selected option: " + option);

            option.action().execute();
        }
    }

    private void showPrompt() {
        System.out.println(name + " - select option:");
        for (Option option : options) {
            System.out.printf("[%s] - %s\n", option.selector(), option.description());
        }
    }

    private String getUserSelection() {
        System.out.print("select>");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

//    co ma się dziać?
//    1. print prompt
//    2. take user input
//    3. handle input
//            print results
//            re-prompt || following actions prompt ||
//    4. navigation ( back / forward )

}
