package au.edu.jcu.educationalgame;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// User Model following MVC design pattern, for storing user score in SQLite DB,
// implements Parcelable to transfer between activities.
public class UserModel implements Parcelable {
    private int id;
    private String userName;
    // 6 digits PIN as password, store as String in DB
    private String pinNumber;
    private int reflexScore;
    private int mathScore;

    // Constructor for creating new users
    public UserModel(String userName, String password) {
        this.userName = userName;
        this.pinNumber = password;
        reflexScore = 0;
        mathScore = 0;
    }

    // Constructor for creating user with retrieved data from DB, to rank on Scoreboard
    public UserModel(String userName, int reflexScore, int mathScore) {
        this.userName = userName;
        this.reflexScore = reflexScore;
        this.mathScore = mathScore;
    }

    public UserModel(int id, String userName, String pinNumber, int reflexScore, int mathScore) {
        this.id = id;
        this.userName = userName;
        this.pinNumber = pinNumber;
        this.reflexScore = reflexScore;
        this.mathScore = mathScore;
    }

    protected UserModel(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        pinNumber = in.readString();
        reflexScore = in.readInt();
        mathScore = in.readInt();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public int getReflexScore() {
        return reflexScore;
    }

    public void setReflexScore(int reflexScore) {
        this.reflexScore = reflexScore;
    }

    public int getMathScore() {
        return mathScore;
    }

    public void setMathScore(int mathScore) {
        this.mathScore = mathScore;
    }

    @NonNull
    @Override
    public String toString() {
        return userName + ": Reflex Score " + reflexScore + "; Math Score: " + mathScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeString(pinNumber);
        parcel.writeInt(reflexScore);
        parcel.writeInt(mathScore);
    }
}
