import model.utils.EventReceiver;
import model.utils.EventSource;

public class ObserverTest {
    static class DummyObserver implements EventReceiver {

        @Override
        public void update(EventSource o, Object arg) {
            System.out.println("UPDATE!");
        }
    }

    static class DummyObservable extends EventSource {
        public void change() {
            System.out.println("SHOULD CALL OBSERVERS!");
            this.notifyAllReceivers();
        }
    }

    public static void main(String[] args) {
        DummyObservable dummy = new DummyObservable();
        dummy.addReceiver(new DummyObserver());
        dummy.change();
    }
}
