package model.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class EventSource implements Serializable {
    private transient ArrayList<EventReceiver> receivers;

    public EventSource() {
        receivers = new ArrayList<>();
    }

    public void addReceiver(EventReceiver rec) {
        receivers.add(rec);
    }

    public void removeReceiver(EventReceiver rec) {
        receivers.remove(rec);
    }


    public void notifyAllReceivers() {
        for (EventReceiver rec: receivers) {
            rec.update(this, null);
        }
    }


    public void notifyAllReceivers(Object arg) {
        for (EventReceiver rec: receivers) {
            rec.update(this, arg);
        }
    }
}
