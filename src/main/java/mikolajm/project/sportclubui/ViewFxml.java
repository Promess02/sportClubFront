package mikolajm.project.sportclubui;

public enum ViewFxml {
    LOGIN("login.fml"),
    MAINVIEW("mainview.fxml");

    private final String name;

    ViewFxml(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
