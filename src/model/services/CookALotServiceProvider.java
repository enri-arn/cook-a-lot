// Questa classe realizza un meccanismo di dependency-injection utilizzando un
// pattern Strategy. I servizi sono definiti tramite interfacce (es. CookALotDataManager)
// e ciascun servizio può avere diverse implementazioni, fra cui gli stub per il testing
// (es. CookALotDataManagerStub). E' responsabilità del ServiceProvider:
// (1) decidere quale implementazione di ciascun servizio istanziare
// (2) fornire le istanze dei servizi alle altre classi

package model.services;

public class CookALotServiceProvider {
    private static CookALotServiceProvider singleInstance = new CookALotServiceProvider();
    public static CookALotServiceProvider getInstance() {
        return CookALotServiceProvider.singleInstance;
    }

    private CookALotDataManager dataManager;

    private CookALotServiceProvider() {
        // I servizi sono inizializzati e istanziati qui.
        // Qui viene stabilito quale versione di un dato servizio si intende usare
        dataManager = new CookALotDataManagerStub();
    }

    public CookALotDataManager getDataManager() {
        return dataManager;
    }
}
