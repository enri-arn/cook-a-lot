package model.businesslogic.exception;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 * Eccezione che si verifica nel caso in cui un cuoco non e' presente in un turno.
 */
public class CookNotInShiftException extends UseCaseLogicException {

    /**
     * Costruttore dell'eccezione.
     */
    public CookNotInShiftException() {
        super("Il cuoco non e' nel turno corrente");
    }
}
