package io.github.teccheck.gear360app.live;

public class VrotData {
    private float mPitch;
    private float mRoll;
    private long mTimeStamp;
    private float mYaw;

    public VrotData() {
        this.mYaw = 0.0F;
        this.mPitch = 0.0F;
        this.mRoll = 0.0F;
        this.mTimeStamp = 0L;
    }

    public VrotData(float yaw, float pitch, float roll) {
        this.mYaw = yaw;
        this.mPitch = pitch;
        this.mRoll = roll;
        this.mTimeStamp = 0L;
    }

    public VrotData(long timeStamp, float yaw, float pitch, float roll) {
        this.mYaw = yaw;
        this.mPitch = pitch;
        this.mRoll = roll;
        this.mTimeStamp = timeStamp;
    }

    public boolean compare(VrotData data) {
        return this.mYaw == data.mYaw && this.mPitch == data.mPitch && this.mRoll == data.mRoll && this.mTimeStamp == data.mTimeStamp;
    }

    public float getPitch() {
        return this.mPitch;
    }

    public float getRoll() {
        return this.mRoll;
    }

    public long getTimeStamp() {
        return this.mTimeStamp;
    }

    public float getYaw() {
        return this.mYaw;
    }

    public void set(float yaw, float pitch, float roll) {
        this.mYaw = yaw;
        this.mPitch = pitch;
        this.mRoll = roll;
    }

    public void set(long timeStamp, float yaw, float pitch, float roll) {
        this.mYaw = yaw;
        this.mPitch = pitch;
        this.mRoll = roll;
        this.mTimeStamp = timeStamp;
    }

    public String toString() {
        return "TimeStamp: " + this.mTimeStamp + "  Yaw: " + this.mYaw + " Pitch: " + this.mPitch + " Roll: " + this.mRoll;
    }
}