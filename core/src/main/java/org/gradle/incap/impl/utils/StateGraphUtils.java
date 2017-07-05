package org.gradle.incap.impl.utils;

import com.gradle.incap.LogWrapper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.gradle.incap.impl.data.StateGraph;

public final class StateGraphUtils {
    private static final String STATE_GRAPH_FILE_PATH = "stateGraph.txt";

    public static void saveToFile(StateGraph stateGraph) {
        if (stateGraph == null) {
            LogWrapper.log("state graph is null");
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(STATE_GRAPH_FILE_PATH);
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
            oos.writeObject(stateGraph);
        } catch (IOException e) {
            handleErrorSavingStateGraph(e);
        }
    }

    private static void handleErrorSavingStateGraph(Exception e) {
        // TODO - properly handle exception, ask users to retry?
        e.printStackTrace();
    }

    public static StateGraph readFromFile() {
        StateGraph stateGraph = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(STATE_GRAPH_FILE_PATH);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            stateGraph = (StateGraph) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            handleErrorReadingStateGraph(e);
        }
        return stateGraph;
    }

    private static void handleErrorReadingStateGraph(Exception e) {
        // TODO - properly handle exception by wipping and regenerating state graph, or just fail and let users know
        LogWrapper.log("cannot read state graph");
        e.printStackTrace();
    }
}
