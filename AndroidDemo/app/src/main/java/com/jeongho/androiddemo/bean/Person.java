package com.jeongho.androiddemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jeongho on 16/7/11.
 */
public class Person implements Parcelable{
    private String mName;
    private int mAge;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(mAge);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>(){

        @Override
        public Person createFromParcel(Parcel source) {
            Person person = new Person();
            person.setName(source.readString());
            person.setAge(source.readInt());
            return person;
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
