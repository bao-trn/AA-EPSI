package utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import geometry.FormGeo;
import states.SelectedState;

import javax.swing.*;

public class FileUtils extends JPanel{

    private String lastFichier = ".";


    // ENREGISTRER FICHIER
    public static void save(ObjectOutputStream out, List<FormGeo> geoFormList) {
        try {
            for (FormGeo f : geoFormList) {
                f.setSelected(new SelectedState());
                out.writeObject(f);

            }

        } catch (IOException io) {
            System.exit(1);
        }
    }

    public static void readInfo(ObjectInputStream in, List<FormGeo> geoFormList) throws IOException, ClassNotFoundException {
        geoFormList.clear();

        Object object = in.readObject();
        if (object instanceof List) {
            ((List<?>) object).forEach(shape -> geoFormList.add((FormGeo) shape));
        }
        in.close();
        //repaint()
    }


}
