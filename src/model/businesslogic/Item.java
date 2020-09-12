package model.businesslogic;

import model.services.CookALotServiceProvider;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico
 */
public class Item implements Cloneable {

    /**
     * Descrizione della voce del menu' e piatto di riferimento.
     */
    private String description;
    private Dish dish;

    /**
     * Identificatore della voce del menu'.
     */
    private String id;

    /**
     * Costruttore.
     *
     * @param description descrizione associata alla voce.
     * @param dish piatto associato alla voce.
     */
    public Item(String description, Dish dish) {
        this.setDescription(description);
        this.setDish(dish);
        this.id = CookALotServiceProvider.getInstance().getDataManager().generateItemId();
    }

    /**
     * Costruttore per i test.
     *
     * @param id identificatore
     * @param description descrizione associata alla voce.
     * @param dish piatto associato alla voce.
     */
    public Item(String id, String description, Dish dish) {
        this.id = id;
        this.setDescription(description);
        this.setDish(dish);
    }

    /**
     * Ottiene l'id della voce.
     *
     * @return l'id della voce.
     */
    public String getId() {
        return id;
    }

    /**
     * Ottiene la descrizione della voce.
     *
     * @return la descrizione della voce.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Ottiene il piatto associato alla voce.
     *
     * @return il piatto associato alla voce.
     */
    public Dish getDish() {
        return dish;
    }

    /**
     * Imposta la descrizione della voce.
     *
     * @param description descrizione da impostare.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Imposta il piatto associato alla voce.
     *
     * @param dish piatto da impostare.
     */
    public void setDish(Dish dish) {
        this.dish = dish;
    }

    /**
     * Clona l'oggetto voce copiandone tutti i parametri ma imposta un nuovo identificatore.
     *
     * @return una nuova voce clone della voce di partenza.
     */
    @Override
    public Item clone() {
        Item it;
        try {
            it = (Item) super.clone();
            it.id = CookALotServiceProvider.getInstance().getDataManager().generateItemId();
            return it;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Stampa a video la voce mostrandone la descrizione.
     *
     * @return una stringa contenente la descrizione della voce.
     */
    @Override
    public String toString() {
        return this.getDescription();
    }

}
