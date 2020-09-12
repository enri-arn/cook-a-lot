package model.businesslogic.exception;

/**
 * @author Arnaudo Enrico, Bonino Samuele, Burdisso Enrico.
 * Eccezione che si verifica nel caso in cui il tempo di attivita' del cuoco supera il tempo di turno.
 */
public class ShiftOverTimeException extends UseCaseLogicException {

    /**
     * Costruttore dell'eccezione.
     */
    public ShiftOverTimeException() {
        super("Il tempo di attivita' del cuoco \nsupera il tempo del turno.");
    }
}
