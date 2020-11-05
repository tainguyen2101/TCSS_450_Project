package edu.uw.group1app.ui.contacts;

import java.util.ArrayList;

public class Person {

    public Person(final String name, final String number) {
        this.mName = name;
        this.mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public String getNumber() {
        return mNumber;
    }

    private final String mName;
    private final String mNumber;

    public static ArrayList<Person> createContactList (int size) {
        ArrayList<Person> contacts = new ArrayList<Person>();
        for (int i = 1; i < size; i++) {
            contacts.add(new Person("Ford", "12345"));
        }
        return contacts;
    }

}
