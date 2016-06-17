import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

public class AutoFilterDocument extends PlainDocument
{
    JComboBox comboBox;
    ComboBoxModel model;
    DefaultComboBoxModel filteredModel;
    JTextComponent editor;
    // flag to indicate if setSelectedItem has been called
    // subsequent calls to remove/insertString should be ignored
    boolean selecting = false;

    public void updateModel(List<String> data)
    {
        model = new DefaultComboBoxModel(data.toArray());
        selecting = true;
        comboBox.setModel(model);
        editor.select(0, 0);
        editor.setCaretPosition(editor.getText().length());
        comboBox.setSelectedItem(null);
        selecting = false;
    }

    public void updateModel(DefaultComboBoxModel filteredModel)
    {
        selecting = true;
        comboBox.setModel(filteredModel);
        editor.select(0, 0);
        editor.setCaretPosition(editor.getText().length());
        selecting = false;
    }

    public AutoFilterDocument(final JComboBox comboBox)
    {
        this.comboBox = comboBox;
        model = comboBox.getModel();
        editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException
    {
        if (selecting)
        {
            return;
        }
        String text = getText(0, offs);

        filteredModel = new DefaultComboBoxModel();
        // iterate over all items
        for (int i = 0, n = model.getSize(); i < n; i++)
        {
            Object currentItem = model.getElementAt(i);
            if (currentItem.toString().contains(text))
            {
                filteredModel.addElement(currentItem);
            }
        }
        updateModel(filteredModel);
        super.remove(offs, len);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
    {
        // return immediately when selecting an item
        if (selecting || str == null || str.length() == 0)
        {
            return;
        }

        super.insertString(offs, str, a);
        String text = getText(0, offs); 
        lookupItem(text + str);
    }

    private Object lookupItem(String pattern)
    {
        Object selectedItem = comboBox.getModel().getSelectedItem();
        if (selectedItem != null && selectedItem.toString().toLowerCase().trim().contains(pattern.toLowerCase().trim()))
        {
            filteredModel = filter(pattern);
            updateModel(filteredModel);
            comboBox.setSelectedItem(selectedItem);
            return selectedItem;
        }
        else
        {
            filteredModel = new DefaultComboBoxModel();
            for (int i = 0, n = model.getSize(); i < n; i++)
            {
                Object currentItem = model.getElementAt(i);
                if (currentItem.toString().toLowerCase().trim().contains(pattern.toLowerCase().trim()))
                {
                    filteredModel.addElement(currentItem);
                }
            }
        }
        updateModel(filteredModel);
        if (filteredModel.getSize() > 0)
        {
            return filteredModel.getElementAt(0);
        }
        else
        {
            return null;
        }
    }

    public DefaultComboBoxModel filter(String pattern)
    {
        filteredModel = new DefaultComboBoxModel();
        // iterate over all items
        for (int i = 0, n = model.getSize(); i < n; i++)
        {
            Object currentItem = model.getElementAt(i);
            // current item starts with the pattern?
            if (currentItem.toString().toLowerCase().trim().contains(pattern.toLowerCase().trim()))
            {
                filteredModel.addElement(currentItem);
            }
        }
        return filteredModel;
    }

    private static void createAndShowGUI()
    {
        // the combo box (add/modify items if you like to)
        JComboBox comboBox = new JComboBox(new Object[]
        {
            "Arte rupestre della Valcamonica", "Centro storico di Roma", "Santa Maria delle Grazie", "Centro storico di Firenze", "Venezia e la sua laguna", "La piazza del Duomo di Pisa", "Centro storico di San Gimignano", "I Sassi di Matera", "La città di Vicenza e le Ville del Palladio nel Veneto", "Centro storico di Siena", "Centro storico di Napoli", "Crespi d’Adda", "Ferrara città del Rinascimento e il suo delta del Po", "Castel del Monte", "I trulli di Alberobello", "Monumenti paleocristiani di Ravenna", "Centro storico della città di Pienza", "Aree archeologiche di Pompei, Ercolano e Torre Annunziata", "Il Palazzo Reale del XVII sec. di Caserta con il parco, l’Acquedotto vanvitelliano e il Complesso di S. Leucio", "Costiera Amalfitana", "Modena: Cattedrale, Torre Civica e Piazza Grande", "Portovenere, Cinque Terre e Isole di Palmaria, Tino e Tinetto", "Residenze Sabaude", "Su Nuraxi di Barumini", "Area archeologica di Agrigento", "Villa romana del Casale a Piazza Armerina", "Orto Botanico di Padova", "Area archeologica di "
            + "Aquileia e basilica Patriarcale", "Centro storico di Urbino", "Parco Nazionale del Cilento", "Vallo di Diano", "Certosa di Padula", "Villa Adriana a Tivoli (Roma)", "Assisi, la Basilica di San Francesco e altri siti francescani", "Isole Eolie", "Città di Verona", "Villa d'Este a Tivoli (Roma)", "Città Barocche del Val di Noto", "Sacri Monti di Piemonte e Lombardia", "Monte San Giorgio", "Val d'Orcia", "Necropoli etrusche di Cerveteri e Tarquinia", "Siracusa e la Necropoli rocciosa di Pantalica", "Genova: Le Strade Nuove and the system of the Palazzi dei Rolli", "Mantova e Sabbioneta", "Ferrovia retica nel territorio di Albula/Bernina (Italia/Svizzera)", "Dolomiti", "I longobardi in Italia. Luoghi di potere", "Siti palafitticoli preistorici delle alpi", "Centro storico della città di Salisburgo", "Palazzo e giardini di Schönbrunn", "Panorama culturale di Hallstatt-Dachstein, Salzkammergut", "Ferrovia del Semmering", "Centro storico della città di Graz e Castello Eggenberg", "Panorama culturale della Wachau", "Centro storico di Vienna",
            "Panorama cultura del lago di Neusiedl (in comune con l'Ungheria)", "Antichi insediamenti sulle Alpi", "Parco nazionale Kakadu", "Grande barriera corallina", "Regione dei Laghi Willandra", "Regione selvaggia della Tasmania", "Lord Howe Island", "Riserve della foresta pluviale centro orientale", "Parco nazionale Uluru-Kata Tjuta", "Tropici del Queensland", "Baia degli squali", "Isola di Fraser", "Siti australiani dei mammiferi fossili (Riversleigh/Naracoorte)", "Isole Heard e McDonald", "Isola Macquarie", "Area delle Greater Blue Mountains", "Parco nazionale Purnululu", "Royal Exhibition Building e i Carlton Gardens", "Teatro dell'opera di Sydney", "Undici penitenziari costruiti tra il XVIII e il XIX secolo dall'impero britannico", "Costa di Ningaloo", "La Grande muraglia cinese", "Monte Taishan, provincia dello Shandong", "Palazzi Imperiali delle dinastie Ming e Qing a Pechino (Città proibita) e Shenyang (Palazzo Mukden)", "Grotte di Mogao a Dunhuang", "Provincia del Gansu", "Mausoleo del primo Imperatore Qin a Xi'an provincia di Shaanxi",
            "Sito dell'uomo di Pechino a Zhoukoudian Municipalità di Pechino", "Monti Huangshan provincia di Anhui", "Valle del Jiuzhaigou area di interesse scenico e storico provincia dello Sichuan", "Huanglong area di interesse scenico e storico provincia dello Sichuan", "Wulingyuan area di interesse scenico e storico provincia dello Hunan", "Località montana e templi circostanti Chengde provincia di Hebei", "Tempio e cimitero di Confucio e maniero della famiglia Kong a Qufu provincia dello Shandong", "Antico complesso di edifici nelle Monti Wudang provincia di Hubei", "Insieme storico del Palazzo del Potala Lhasa Tibet", "Parco nazionale Lushan provincia dello Jiangxi", "Area scenica del Monte Emei compreso il Buddha gigante di Leshan provincia dello Sichuan", "Città vecchia di Lijiang provincia di Yunnan", "Antica città di Ping Yao provincia di Shanxi", "Giardini classici di Suzhou provincia di Jiangsu", "Palazzo d'Estate e giardino imperiale di Pechino", "Il Tempio del paradiso: un altare sacrificale imperiale a Pechino",
            "Monte Wuyi provincia di Fujian", "Incisioni rupestri di Dazu provincia di Sichuan", "Monte Qincheng e il sistema di irrigazione del Dujiangyan provincia di Sichuan", "Antichi villaggi nell'Anhui meridionale-Xidi e Hongcun", "Grotte di Longmen vicino Luoyang provincia di Henan", "Tombe imperiali delle dinastie Ming e Qing", "Grotte di Yungang a Datong provincia di Shanxi", "Area protetta dei tre fiumi paralleli dello Yunnan", "Città capitali e tombe dell'antico regno Goguryeo", "Il centro storico di Macao", "Santuari del panda gigante nella provincia di Sichuan", "Sito archeologico di Yin Xu nella provincia di Henan", "Diaolou e i villaggi del Kaiping", "Paesaggio carsico della Cina meridionale", "Tulou di Fujian", "Parco nazionale del monte Sanqingshan", "Monte Wutai[1]", "Monumenti storici di Dengfeng", "Paesaggio culturale del lago dell'ovest di Hangzhou", "Sito fossile di Chengjiang", "Sito di Xanadu", "Mont Saint-Michel e la sua baia", "Cattedrale di Chartres", "Palazzo e Parco di Versailles", "Basilica e collina di Vézelay",
            "Grotte di Lascaux nella valle del Vézère", "Palazzo e Parco di Fontainebleau", "Cattedrale Notre-Dame di Amiens", "Teatro romano e dintorni e l'arco di Orange", "Monumenti Romani e Romanici di Arles", "Abbazia di Fontenay", "Saline Reali di Arc-et-Senans e Saline di Salins-les-Bains", "Place Stanislas Place de la Carrière e Place d'Alliance a Nancy", "Abbazia di Saint-Savin sur Gartempe", "Golfo di Porto Capo Girolata Riserva naturale di Scandola e Calanchi di Piana in Corsica", "Acquedotto romano di Pont du Gard", "Grande île nel centro di Strasburgo", "Parigi Argini della Senna", "Cattedrale di Notre-Dame ex Abbazia di Saint-Remi e Palazzo di Tau a Reims", "Cattedrale di Bourges", "Centro storico di Avignone", "Canal du Midi", "Storica città fortificata di Carcassonne", "Pirenei-Mont Perdu", "Strade francesi per Santiago de Compostela", "Sito storico di Lione", "Giurisdizione di Saint-Émilion", "Campanili di Belgio e Francia", "La Valle della Loira tra Sully-sur-Loire e Chalonnes-sur-Loire", "Provins città delle fiere medioevali",
            "La città di Le Havre", "Bordeaux Porto della Luna", "Fortificazioni di Vauban", "Lagune della Nuova Caledonia", "Città episcopale di Albi", "Antichi insediamenti sulle Alpi", "Causses e Cevenne paesaggio culturale agro-pastorale", "Bacino minerario del Nord-Passo di Calais", "Cattedrale di Aquisgrana", "Cattedrale di Spira", "Residenza di Würzburg con i giardini di corte e la piazza della residenza", "Chiesa del pellegrinaggio di Wies", "Castelli di Augustusburg e Falkenlust a Brühl", "Cattedrale di Santa Maria e Chiesa di San Michele a Hildesheim", "Monumenti Romani Cattedrale di San Pietro e Chiesa di Nostra Signora a Treviri", "Città anseatica di Lubecca", "Confini dell'Impero romano: Vallo di Adriano-Limes germanico-retico-Vallo Antonino", "Palazzi e parchi di Potsdam e Berlino", "Abbazia e vecchia cattedrale di Lorsch", "Miniere di Rammelsberg e città storica di Goslar", "Complesso monastico di Maulbronn", "Città di Bamberga", "Chiesa collegiata castello e città vecchia di Quedlinburg", "Fonderie di Völklingen",
            "Sito fossile del Pozzo di Messel", "Duomo di Colonia", "Il Bauhaus e i suoi siti a Weimar e Dessau", "Città luterane di Eisleben e Wittenberg", "Weimar classica", "Isola dei musei (Museumsinsel) Berlino", "Castello di Wartburg", "Regno giardino di Dessau-Wörlitz", "Isola di Reichenau", "Complesso industriale delle Miniere di carbone dello Zollverein ad Essen", "Gola del Reno fra Coblenza e Bingen", "Centri storici di Stralsund e Wismar", "Il municipio e la statua sulla piazza del mercato di Brema", "Il parco di Bad Muskau / Park Muzakowski", "La valle dell'Elba a Dresda", "Città vecchia di Ratisbona", "Foreste primordiali dei faggi dei Carpazi e Germania", "Residenze in stile moderno di Berlino", "Wattenmeer", "Officine Fagus ad Alfeld an der Leine", "Antichi insediamenti sulle Alpi", "Teatro dell'Opera margraviale di Bayreuth", "Monumenti buddhisti nella regione di Horyu-ji", "Castello di Himeji", "Shirakami-Sanchi", "Yakushima", "Monumenti storici dell'antica Kyoto (città di Kyoto Uji ed Ōtsu)", "Villaggi storici di Shirakawa-go e Gokayama",
            "Memoriale della pace di Hiroshima", "Santuario shintoista di Itsukushima"
        });
        comboBox.setEditable(true);
        JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        editor.setDocument(new AutoFilterDocument(comboBox));

        // create and show a window containing the combo box
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.getContentPane().add(comboBox);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
}