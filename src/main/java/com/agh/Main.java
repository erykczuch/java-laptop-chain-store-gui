package com.agh;

import com.agh.app.DataGenerator;
import com.agh.app.StoreFacade;
import com.agh.ui.StoreFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    static void main() {
        StoreFacade store = new StoreFacade(DataGenerator.getInstance().getStore());
        StoreFrame frame = new StoreFrame(store);
        frame.setVisible(true);
    }
}
