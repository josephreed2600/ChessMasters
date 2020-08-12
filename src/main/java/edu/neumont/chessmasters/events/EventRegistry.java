package edu.neumont.chessmasters.events;

import edu.neumont.chessmasters.annotations.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class EventRegistry {

    private static ArrayList<Object> registeredClasses = new ArrayList<>();

    public static ArrayList<Method> getMethods(Object obj, Event event) {
        ArrayList<Method> ret = new ArrayList<>();
        for (Method method : obj.getClass().getMethods()) {
            EventHandler handler = method.getAnnotation(EventHandler.class);
            if (handler == null)
                continue;

            Class<? extends Event> eventType = (Class<? extends Event>) method.getParameterTypes()[0];
            if (eventType.equals(event.getClass()))
                ret.add(method);
        }

        return ret;
    }

    public static void registerEvents(Object obj) {
        boolean hasEvents = false;
        for (Method method : obj.getClass().getMethods()) {
            EventHandler handler = method.getAnnotation(EventHandler.class);
            if (handler == null)
                continue;

            hasEvents = true;
        }

        if (hasEvents) {
            if (!registeredClasses.contains(obj))
                registeredClasses.add(obj);
        }
    }

    public static void callEvents(Event event) {
        for (Object obj : registeredClasses) {
            for (Method method : getMethods(obj, event)) {
                try {
                    method.invoke(obj, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.out.println("Could not call event '" + event.getName() + "'. Please see the following error:");
                    e.printStackTrace();
                }
            }
        }
    }

}
