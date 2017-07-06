package org.gradle.incap.impl.utils;

import com.gradle.incap.LogWrapper;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.gradle.incap.impl.data.StateGraph;

public class StateGraphUtils {
    protected static final String STATE_GRAPH_FILE_PATH = "stateGraph.txt";

    public void saveToFile(StateGraph stateGraph) {
        if (stateGraph == null) {
            LogWrapper.log("state graph is null");
            return;
        }

        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(STATE_GRAPH_FILE_PATH);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(stateGraph);
        } catch (IOException e) {
            handleErrorSavingStateGraph(e);
        } finally {
            close(objectOutputStream);
            close(fileOutputStream);
        }
    }

    private static void handleErrorSavingStateGraph(Exception e) {
        // TODO - properly handle exception, ask users to retry?
        e.printStackTrace();
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException e) {
                LogWrapper.log("Error closing closeable " + closeable);
            }
        }
    }

    public StateGraph readFromFile() {
        StateGraph stateGraph = null;
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        try {
            fileInputStream = new FileInputStream(STATE_GRAPH_FILE_PATH);
            objectInputStream = new ObjectInputStream(fileInputStream);
            stateGraph = (StateGraph) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            handleErrorReadingStateGraph(e);
        } finally {
            close(objectInputStream);
            close(fileInputStream);
        }

        return stateGraph;
    }

    private static void handleErrorReadingStateGraph(Exception e) {
        // TODO - properly handle exception by wipping and regenerating state graph, or just fail and let users know
        LogWrapper.log("cannot read state graph");
        e.printStackTrace();
    }
}
