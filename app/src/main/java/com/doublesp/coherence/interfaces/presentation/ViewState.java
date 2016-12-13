package com.doublesp.coherence.interfaces.presentation;

import org.parceler.Parcel;

@Parcel
public class ViewState {

    int state; // {R.id.state_idle, R.id.state_refreshing, R.id.state_loaded}
    OPERATION operation;
    int start;
    int count;

    public ViewState() {
    }

    public ViewState(int state) {
        this.state = state;
        this.operation = OPERATION.NOOP;
        this.start = -1;
        this.count = -1;
    }

    public ViewState(int state, OPERATION operation) {
        this.state = state;
        this.operation = operation;
        this.start = -1;
        this.count = -1;
    }

    public ViewState(int state, OPERATION operation, int start) {
        this.state = state;
        this.operation = operation;
        this.start = start;
        this.count = -1;
    }

    public ViewState(int state, OPERATION operation, int start, int count) {
        this.state = state;
        this.operation = operation;
        this.start = start;
        this.count = count;
    }

    public int getState() {
        return state;
    }

    public OPERATION getOperation() {
        return operation;
    }

    public int getStart() {
        return start;
    }

    public int getCount() {
        return count;
    }

    public enum OPERATION {
        NOOP, RELOAD, ADD, INSERT, UPDATE, REMOVE, CLEAR
    }
}
