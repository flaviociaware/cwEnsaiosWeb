package opencv;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCloneImage;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;
import static com.googlecode.javacv.cpp.opencv_core.cvGetMat;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExtratorCaracteristicas {

    public static void main(String[] args) throws IOException {
        int proximaImagem, classePersonagem;
        String classePersonagemString;
        double red, green, blue;

        // Cabeçalho do arquivo Weka
	String exportacao = "@relation caracteristicas\n\n";
	exportacao += "@attribute laranja_camisa_bart real\n";
	exportacao += "@attribute azul_calcao_bart real\n";
	exportacao += "@attribute azul_sapato_bart real\n";
	exportacao += "@attribute marrom_boca_homer real\n";
	exportacao += "@attribute azul_calca_homer real\n";
	exportacao += "@attribute cinza_sapato_homer real\n";
	exportacao += "@attribute classe {Bart, Homer}\n\n";
	exportacao += "@data\n";
        
        // Diretório onde estão armazenadas as imagens
        File diretorio = new File("src\\imagens");
        File[] arquivos = diretorio.listFiles();

        // Características do Homer e Bart
        float laranjaCamisaBart, azulCalcaoBart, azulSapatoBart;
        float azulCalcaHomer, marromBocaHomer, cinzaSapatoHomer;
        
        // Definição do vetor de características
        float[][] caracteristicas = new float[293][7];

        // Percorre todas as imagens do diretório
        for (int i = 0; i < arquivos.length; i++) {
            laranjaCamisaBart = 0;
            azulCalcaoBart = 0;
            azulSapatoBart = 0;
            azulCalcaHomer = 0;
            marromBocaHomer = 0;
            cinzaSapatoHomer = 0;

            // Carrega cada imagem do diretório
            IplImage imagemOriginal = cvLoadImage(diretorio.getAbsolutePath() + "\\" + arquivos[i].getName());
            CvSize tamanhoImagemOriginal = cvGetSize(imagemOriginal);
            
            // Imagem processada - tamanho, profundidade de cores e número de canais de cores
            IplImage imagemProcessada = cvCreateImage(tamanhoImagemOriginal, IPL_DEPTH_8U, 3);
            imagemProcessada = cvCloneImage(imagemOriginal);

            // Definição da classe - Homer ou Bart
            if (arquivos[i].getName().charAt(0) == 'b') {
                classePersonagem = 0;
                classePersonagemString = "Bart";
            } else {
                classePersonagem = 1;
                classePersonagemString = "Homer";
            }

            // Matriz multi-canal
            CvMat mtx = CvMat.createHeader(imagemProcessada.height(), imagemProcessada.width());
            CvScalar scalarImagemProcessada = new CvScalar();
            cvGetMat(imagemProcessada, mtx, null, 0);

            // Varre a imagem pixel a pixel
            for (int altura = 0; altura < imagemProcessada.height(); altura++) {
                for (int largura = 0; largura < imagemProcessada.width(); largura++) {
                    
                    // Extração do RGB de cada pixel da imagem
                    CvScalar scalarExtraiRgb = cvGet2D(imagemProcessada, altura, largura);
                    blue = scalarExtraiRgb.val(0);
                    green = scalarExtraiRgb.val(1);
                    red = scalarExtraiRgb.val(2);

                    // Camisa laranja do Bart                    
                    if (blue >= 11 && blue <= 22 && 
                            green >= 85 && green <= 105 && 
                            red >= 240 && red <= 255) {                       
                        scalarImagemProcessada.setVal(0, 0);
                        scalarImagemProcessada.setVal(1, 255);
                        scalarImagemProcessada.setVal(2, 128);
                        cvSet2D(mtx, altura, largura, scalarImagemProcessada);
                        laranjaCamisaBart++;
                    }

                    // Calção azul do Bart (metade de baixo da imagem)
                    if (altura > (imagemProcessada.height() / 2)) {
                        if (blue >= 125 && blue <= 170 && green >= 0 && green <= 12 && red >= 0 && red <= 20) {
                            scalarImagemProcessada.setVal(0, 0);
                            scalarImagemProcessada.setVal(1, 255);
                            scalarImagemProcessada.setVal(2, 128);
                            cvSet2D(mtx, altura, largura, scalarImagemProcessada);
                            azulCalcaoBart++;
                        }
                    }

                    // Sapato do Bart (parte inferior da imagem)
                    if (altura > (imagemProcessada.height() / 2) + (imagemProcessada.height() / 3)) {
                        if (blue >= 125 && blue <= 140 && green >= 3 && green <= 12 && red >= 0 && red <= 20) {
                            scalarImagemProcessada.setVal(0, 0);
                            scalarImagemProcessada.setVal(1, 255);
                            scalarImagemProcessada.setVal(2, 128);
                            cvSet2D(mtx, altura, largura, scalarImagemProcessada);
                            azulSapatoBart++;
                        }
                    }

                    // Calça azul do Homer
                    if (blue >= 150 && blue <= 180 && green >= 98 && green <= 120 && red >= 0 && red <= 90) {
                        scalarImagemProcessada.setVal(0, 0);
                        scalarImagemProcessada.setVal(1, 255);
                        scalarImagemProcessada.setVal(2, 255);
                        cvSet2D(mtx, altura, largura, scalarImagemProcessada);
                        azulCalcaHomer++;
                    }

                    // Boca do Homer (pouco mais da metade da imagem)
                    if (altura < (imagemProcessada.height() / 2) + (imagemProcessada.height() / 3)) {
                        if (blue >= 95 && blue <= 140 && green >= 160 && green <= 185 && red >= 175 && red <= 200) {
                            scalarImagemProcessada.setVal(0, 0);
                            scalarImagemProcessada.setVal(1, 255);
                            scalarImagemProcessada.setVal(2, 255);
                            cvSet2D(mtx, altura, largura, scalarImagemProcessada);		
                            marromBocaHomer++;
                        }
                    }

                    // Sapato do Homer (parte inferior da imagem)
                    if (altura > (imagemProcessada.height() / 2) + (imagemProcessada.height() / 3)) {
                        if (blue >= 25 && blue <= 45 && green >= 25 && green <= 45 && red >= 25 && red <= 45) {
                            scalarImagemProcessada.setVal(0, 0);
                            scalarImagemProcessada.setVal(1, 255);
                            scalarImagemProcessada.setVal(2, 255);
                            cvSet2D(mtx, altura, largura, scalarImagemProcessada);
                            cinzaSapatoHomer++;
                        }
                    }
                }
            }

            // Imagem processada de acordo com as características (alteração das cores)
            imagemProcessada = new IplImage(mtx);

            // Normaliza as características pelo número de pixels totais da imagem
            laranjaCamisaBart = (laranjaCamisaBart / (imagemOriginal.height() * imagemOriginal.width())) * 100;
            azulCalcaoBart = (azulCalcaoBart / (imagemOriginal.height() * imagemOriginal.width())) * 100;
            azulSapatoBart = (azulSapatoBart / (imagemOriginal.height() * imagemOriginal.width())) * 100;
            azulCalcaHomer = (azulCalcaHomer / (imagemOriginal.height() * imagemOriginal.width())) * 100;
            marromBocaHomer = (marromBocaHomer / (imagemOriginal.height() * imagemOriginal.width())) * 100;
            cinzaSapatoHomer = (cinzaSapatoHomer / (imagemOriginal.height() * imagemOriginal.width())) * 100;
            
            // Grava as características no vetor de características
            caracteristicas[i][0] = laranjaCamisaBart;
            caracteristicas[i][1] = azulCalcaoBart;
            caracteristicas[i][2] = azulSapatoBart;
            caracteristicas[i][3] = azulCalcaHomer;
            caracteristicas[i][4] = marromBocaHomer;
            caracteristicas[i][5] = cinzaSapatoHomer;
            caracteristicas[i][6] = classePersonagem;

            System.out.println("Laranja camisa Bart: " + caracteristicas[i][0] + " - Azul calção Bart: " + caracteristicas[i][1] + " - Azul sapato Bart: " + caracteristicas[i][2] + " - Azul calça Homer: " + caracteristicas[i][3] + " - Marrom boca Homer: " + caracteristicas[i][4] + " - Preto sapato Homer: " + caracteristicas[i][5] + " - Classe: " + caracteristicas[i][6]);
            exportacao += caracteristicas[i][0] + "," + caracteristicas[i][1] + "," + caracteristicas[i][2] + "," + caracteristicas[i][3] + "," + caracteristicas[i][4] + "," + caracteristicas[i][5] + "," + classePersonagemString + "\n";

            //cvShowImage("Imagem original", imagemOriginal);
            //cvShowImage("Imagem processada", imagemProcessada);
            //proximaImagem = cvWaitKey();
        }

        // Grava o arquivo ARFF no disco
        File arquivo = new File("caracteristicas.arff");
        FileOutputStream f = new FileOutputStream(arquivo);
        f.write(exportacao.getBytes());
        f.close();
    }
}