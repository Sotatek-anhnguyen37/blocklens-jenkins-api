package web_core;


import java.lang.reflect.InvocationTargetException;

public class PageFactoryManager {
    private PageFactoryManager() {
    }

    public static <TPage> TPage get(Class<TPage> pageClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        return pageClass.getDeclaredConstructor().newInstance();

    }
}
