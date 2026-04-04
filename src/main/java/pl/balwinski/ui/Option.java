package pl.balwinski.ui;

public record Option(
        String selector,
        String description,
        UiAction action,
        UiPage nextPage
        ) {

        @Override
        public String toString() {
                return "Option{" +
                        "selector='" + selector + '\'' +
                        ", description='" + description + '\'' +
                        ", action=" + action +
                        ", nextPage=" + nextPage +
                        '}';
        }
}
