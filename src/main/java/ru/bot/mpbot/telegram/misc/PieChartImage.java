package ru.bot.mpbot.telegram.misc;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PieChartImage {
    HashMap<String, Double> data;
    public PieChartImage(HashMap<String, Double> data){
        this.data = data;
    }
    public File drawToFile(String path, String title) throws IOException {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        for (Map.Entry<String, Double> entry: data.entrySet()){
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        JFreeChart chart = ChartFactory.createPieChart(
                title,   // chart title
                dataset,          // data
                true,             // include legend
                false,
                false);
        PiePlot plot =(PiePlot) chart.getPlot();
        plot.setSectionPaint("Ozon", Color.decode("#568ECC"));
        plot.setSectionPaint("WB", Color.decode("#9C2AB0"));
        plot.setLabelFont(Font.decode("Cambria"));
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelBackgroundPaint(Color.WHITE);
        plot.setShadowPaint(null);

        chart.setBorderPaint(Color.WHITE);
        File file = new File(path);
        file.createNewFile();
        ChartUtils.saveChartAsPNG(file, chart, 300, 300);
        return file;
    }

    public static File joinCharts(File f1, File f2){
        try {
            BufferedImage img1 = ImageIO.read(f1);
            BufferedImage img2 = ImageIO.read(f2);
            int offset = 1;
            int width = img1.getWidth() + img2.getWidth() + offset;
            int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
            BufferedImage newImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = newImage.createGraphics();
            Color oldColor = g2.getColor();
            g2.setPaint(Color.WHITE);
            g2.fillRect(0, 0, width, height);
            g2.setColor(oldColor);
            g2.drawImage(img1, null, 0, 0);
            g2.drawImage(img2, null, img1.getWidth() + offset, 0);
            g2.dispose();
            ImageIO.write(newImage, "PNG", f1);
            return f1;
        } catch (IOException e){
            return f1;
        }
    }
}
