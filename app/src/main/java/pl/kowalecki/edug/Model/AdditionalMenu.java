package pl.kowalecki.edug.Model;

public class AdditionalMenu {

    private int menuElementImage;
    private String menuElement;


    public AdditionalMenu(int menuElementImage, String menuElement) {
        this.menuElement = menuElement;
        this.menuElementImage = menuElementImage;
    }

    public String getMenuElement() {
        return menuElement;
    }

    public void setMenuElement(String menuElement) {
        this.menuElement = menuElement;
    }

    public int getMenuElementImage() {
        return menuElementImage;
    }

    public void setMenuElementImage(int menuElementImage) {
        this.menuElementImage = menuElementImage;
    }
}
