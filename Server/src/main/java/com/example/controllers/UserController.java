package com.example.controllers;

import com.example.entities.Pacient;
import com.example.entities.Recept;
import com.example.entities.Receptpreparat;
import com.example.entities.User;
import com.example.handlers.UserHandler;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.xml.sax.SAXException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.Size;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.beans.beancontext.BeanContext;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

@Controller("/Users")
public class UserController {

    @Post(value = "/Register" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> reg(String polis,String f,String i,String o,String email,String pass) {
        if(checkUnique(polis).isEmpty())
        {
            addP(new Pacient(1, f, i, o, polis, pass, email));
            return HttpResponse.ok().body("{\"msg\":\"ok\",\"token\":\""+token()+"\"," +
                    "\"f\":\""+f+"\"," +
                    "\"i\":\""+i+"\"," +
                    "\"o\":\""+o+"\"," +
                    "\"polis\":\""+polis+"\"," +
                    "\"email\":\""+email+"\"" +
                    "}");
        }
        else {
            return HttpResponse.ok().body("{\"msg\":\"neok\"}");
        }
    }
    @Post(value = "/Login" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> log(String polis,String pass) {
        if(!(checkLog(polis,pass).isEmpty()))
        {
            return HttpResponse.ok().body("{\"msg\":\"okLOg\",\"token\":\""+token()+"\"," +
                    "\"f\":\""+checkLog(polis,pass).get(0).f+"\"," +
                    "\"i\":\""+checkLog(polis,pass).get(0).i+"\"," +
                    "\"o\":\""+checkLog(polis,pass).get(0).o+"\"," +
                    "\"polis\":\""+checkLog(polis,pass).get(0).polis+"\"," +
                    "\"email\":\""+checkLog(polis,pass).get(0).email+"\"" +
                    "}");
        }
        else {
            return HttpResponse.ok().body("{\"msg\":\"neok\"}");
        }
    }

    @Post(value = "/CheckTocken", consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> test(String token) {
        if(tokenList.contains(token)) {return HttpResponse.ok().body("{\"msg\":\"ok\"}");}
        else {return HttpResponse.ok().body("{\"msg\":\"Wrong token\"}");}
    }

    //История рецептов пациента
    @Post(value = "/getPac" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> pac(String polis, List<String> filter, String token ) throws ParseException {
        if(tokenList.contains(token))
        {
            if (!(gPac(polis).isEmpty()))
            {
                String response = "";
                Pacient pacient = gPac(polis).get(0);
                if (gRec(pacient.id).isEmpty())
                {
                    return HttpResponse.ok().body("{\"msg\":\"ok\"," +
                            " \"polis\":\""+pacient.polis+"\"," +
                            " \"f\":\""+pacient.f+"\" ," +
                            " \"i\":\""+pacient.i+"\" ," +
                            " \"o\":\""+pacient.o+"\"," +
                            " \"rec\":[]}");
                }
                else {
                    if(filter.isEmpty())
                    {
                        response += "{\"msg\":\"ok\"," +
                                " \"polis\":\""+pacient.polis+"\"," +
                                " \"f\":\""+pacient.f+"\" ," +
                                " \"i\":\""+pacient.i+"\" ," +
                                " \"o\":\""+pacient.o+"\"," +
                                " \"email\":\""+pacient.email+"\"," +
                                " \"recepts\":[";
                        ListIterator<Recept> recepts = gRec(pacient.id).listIterator();
                        while (recepts.hasNext())
                        {
                            Recept recept = recepts.next();
                            recept = updStat(recept);
                            response +="{\"id\":"+recept.id+"," +
                                    "\"dateof\":\""+recept.dateo.substring(0,10)+"\"," +
                                    "\"srok\":"+recept.srok+"," +
                                    "\"status\":\""+recept.status+"\"," +
                                    "\"diagnoz\":\""+recept.diagnoz+"\"," +
                                    "\"fio\":\""+recept.f+" "+recept.i.substring(0,1)+". "+recept.o.substring(0,1)+".\"," +
                                    "\"ecp\":\""+recept.ecp+"\"},";
                        }
                        return HttpResponse.ok().body(response.substring(0,response.length()-1)+"]}");
                    }
                    else {return HttpResponse.ok().body("{\"msg\":\"neok\"}");}
                }
            } else {return HttpResponse.ok().body("{\"msg\":\"neok\"}");}
        } else {return HttpResponse.ok().body("{\"msg\":\"Wrong token\"}");}
    }

    //Список лекарств по рецепту
    @Post(value = "/getLec" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> rec(int idrec,String token) {
        if(tokenList.contains(token))
        {
            if (!(gPrep(idrec).isEmpty()))
            {
                String response = "";
                response += "{ \"leclist\" : [";
                ListIterator<Receptpreparat> preps = gPrep(idrec).listIterator();
                while (preps.hasNext())
                {
                    Receptpreparat receptpreparat = preps.next();
                    response +="{\"id\":\""+receptpreparat.id+"\"," +
                            "\"sppr\":\""+receptpreparat.sppr+"\"," +
                            "\"edizm\":\""+receptpreparat.edizm+"\"," +
                            "\"vipicano\":\""+receptpreparat.vipicano+"\"," +
                            "\"doza\":\""+receptpreparat.doza+"\"," +
                            "\"kolvodoz\":\""+receptpreparat.kolvodoz+"\"," +
                            "\"kolvokurs\":\""+receptpreparat.kolvokurs+"\"," +
                            "\"kurs\":\""+receptpreparat.kurs+"\"," +
                            "\"prep_name\":\""+receptpreparat.prep_name+"\"," +
                            "\"idrec\":\""+receptpreparat.idrec+"\"," +
                            "\"idpre\":\""+receptpreparat.idpre+"\"},";
                }
                return HttpResponse.ok().body(response.substring(0,response.length()-1)+"]}");
            } else {return HttpResponse.ok().body("{\"msg\":\"Не\"}");}
        } else {return HttpResponse.ok().body("{\"msg\":\"Wrong token\"}");}
    }




    //QR генерация
    @Post(value = "/getQR" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> qr(int idrec, String token) throws IllegalBlockSizeException, NoSuchPaddingException,
            IOException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, WriterException, ParseException {
        if(tokenList.contains(token))
        {
            if (!(findRec(idrec).isEmpty())&&updStat(findRec(idrec).get(0)).status.equals("активен"))
            {

                String encoded = Base64.getEncoder().encodeToString(gQR(findRec(idrec).get(0)));
                return HttpResponse.ok().body(encoded);
            }
            else {
                File file = new File("src/main/resources/wrongqr.png");
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String encoded = Base64.getEncoder().encodeToString(fileContent);
                return HttpResponse.ok().body(encoded);
            }
        } else {return HttpResponse.ok().body("{\"msg\":\"Wrong token\"}");}

    }
    //QR проверка
    @Get(value = "/checkQR/{shifr}" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> checkqr(@QueryValue String shifr) throws IllegalBlockSizeException, NoSuchPaddingException,
            BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        int idrec = Integer.parseInt(encr_decr(shifr,false));
        if (!(findRec(idrec).isEmpty()))
        {
            if (!(gPrep(idrec).isEmpty()))
            {
                String response = "";
                response += "{ \"leclist\" : [";
                ListIterator<Receptpreparat> preps = gPrep(idrec).listIterator();
                while (preps.hasNext())
                {
                    Receptpreparat receptpreparat = preps.next();
                    response +="{\"prep_name\":\""+receptpreparat.prep_name+"\"," +
                            "\"vipicano\":\""+receptpreparat.vipicano+"\"},";
                }
                return HttpResponse.ok().body(response.substring(0,response.length()-1)+"]}");
            } else {return HttpResponse.ok().body("{\"msg\":\"Neok\"}");}
        } else {return HttpResponse.badRequest();}
    }

    //Отчет
    @Post(value = "/getOtchet" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> otchet(String startdate, String enddate, String polis, String token) throws ParseException, IllegalBlockSizeException,
            NoSuchPaddingException, IOException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, WriterException, SAXException {
        if(tokenList.contains(token))
        {
            LocalDate sdate = LocalDate.parse(startdate.substring(0,10));
            sdate = sdate.plusDays(1);
            LocalDate edate = LocalDate.parse(enddate.substring(0,10));
            edate = edate.plusDays(1);
            PDFgen(polis,1,true,sdate,edate);
                File file = new File("output.pdf");
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String encoded = Base64.getEncoder().encodeToString(fileContent);
                return HttpResponse.ok().body(encoded);
        } else {return HttpResponse.ok().body("{\"msg\":\"Wrong token\"}");}

    }

    //ВЫдача пдф
    @Post(value = "/getPDF" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> pdf(int idrec,String polis,String token) throws IllegalBlockSizeException, NoSuchPaddingException, IOException,
            BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, WriterException, SAXException, ParseException {
        if(tokenList.contains(token))
        {
            PDFgen(polis,idrec,false,LocalDate.now(),LocalDate.now());
            File file = new File("output.pdf");
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encoded = Base64.getEncoder().encodeToString(fileContent);
            return HttpResponse.ok().body(encoded);
        } else {return HttpResponse.ok().body("{\"msg\":\"Wrong token\"}");}

    }

    //выполнение рецепта
    @Put(value = "/changeSt" , consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<String> idr(int idrec) {
        changeS(idrec);
        return HttpResponse.ok().body("{\"msg\":\"ok\"}");
    }
    //-------------------------------------------------------------

    //Тест
    @Post(value = "/echo", consumes = MediaType.APPLICATION_JSON)
    String echo(@Size(max = 1024) @Body String text) {
        return text;
    }
//-------------------------------------------------------------
//----------------------------------------------------------

    //-------------------------------------------------------------
//    private static final String conn_s = "jdbc:postgresql://45.10.244.15:55532/work100016";
//    private static final String user_s = "work100016";
//    private static final String pass_s = "{FXcadFL99Ncvo?kOMW~";

    private static final String conn_s = "jdbc:postgresql://localhost:5432/recepts";
    private static final String user_s = "postgres";
    private static final String pass_s = "1111";
    private List<String> tokenList = new ArrayList<String>();

    public byte[] gQR(Recept recept) throws WriterException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        StringBuffer sb = new StringBuffer();
        String path = "http://localhost:8080/Users/checkQR/";
        sb.append(path);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(String.valueOf(sb.append(encr_decr(String.valueOf(recept.getId()),true))), BarcodeFormat.QR_CODE, 350, 350);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }
    public Recept updStat(Recept recept) throws ParseException {
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format2.parse(recept.dateo.substring(0,10));
        LocalDate currentdate = LocalDate.now().minusDays(recept.srok);
        Instant instant = currentdate.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
        Date currdate = Date.from(instant);
        if(date.before(currdate)&&recept.status.equals("активен"))
        {
            recept.status = "просрочен";
            return recept;
        }
        return recept;
    }

    public void changeS(int id){
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            PreparedStatement statement = con.prepareStatement("update recept set status = 'обслужен' where idrec = ?");
            statement.setObject (1, id);
            statement.execute();
        } catch (SQLException e){ e.printStackTrace();}
    }
    private List<Receptpreparat> gPrep(int idrec){
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            Statement statement = con.createStatement();
            String q = String.format("SELECT idprerec, sppr, edizm, vipisano, doza, kolvodoz, kolvokurs, kurs, prep_name " +
                    "FROM preparatrecept JOIN preparat ON preparatrecept.idpre = preparat.idpre " +
                    "where idrec = %d",idrec);
            List<Receptpreparat> receptpreparats = new ArrayList<Receptpreparat>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                receptpreparats.add(new Receptpreparat(
                        resultSet.getInt("idprerec"),
                        resultSet.getString("sppr"),
                        resultSet.getString("edizm"),
                        resultSet.getInt("vipisano"),
                        resultSet.getInt("doza"),
                        resultSet.getInt("kolvodoz"),
                        resultSet.getInt("kolvokurs"),
                        resultSet.getString("kurs"),
                        resultSet.getString("prep_name")));
            }
            statement.close();
            con.close();
            return receptpreparats;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    private List<Pacient> gPac(String polis){
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            Statement statement = con.createStatement();
            String q = String.format("SELECT * FROM pacient WHERE polis = '%s'",polis);
            List<Pacient> pacients = new ArrayList<Pacient>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                pacients.add(new Pacient(resultSet.getInt("idpac"),
                        resultSet.getString("f"),
                        resultSet.getString("i"),
                        resultSet.getString("o"),
                        resultSet.getString("polis"),
                        resultSet.getString("pass"),
                        resultSet.getString("email")));
            }
            statement.close();
            con.close();
            return pacients;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    private List<Recept> gRec(int id){
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            Statement statement = con.createStatement();
            String q = String.format("SELECT * FROM recept JOIN doctor ON recept.iddoc = doctor.iddoc WHERE idpac = %d",id);
            List<Recept> recepts = new ArrayList<Recept>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                recepts.add(new Recept(resultSet.getInt("idrec"),
                        resultSet.getString("dateof"),
                        resultSet.getInt("srok"),
                        resultSet.getString("status"),
                        resultSet.getString("diagnoz"),
                        resultSet.getString("qr"),
                        resultSet.getInt("iddoc"),
                        resultSet.getInt("idpac"),
                        resultSet.getString("f"),
                        resultSet.getString("i"),
                        resultSet.getString("o"),
                        resultSet.getString("ecp")
                ));
            }
            statement.close();
            con.close();
            return recepts;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    private List<Recept> findRec(int id){
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            Statement statement = con.createStatement();
            String q = String.format("SELECT * FROM recept JOIN doctor ON recept.iddoc = doctor.iddoc WHERE idrec = %d",id);
            List<Recept> recepts = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                recepts.add(new Recept(resultSet.getInt("idrec"),
                        resultSet.getString("dateof"),
                        resultSet.getInt("srok"),
                        resultSet.getString("status"),
                        resultSet.getString("diagnoz"),
                        resultSet.getString("qr"),
                        resultSet.getInt("iddoc"),
                        resultSet.getInt("idpac"),
                        resultSet.getString("f"),
                        resultSet.getString("i"),
                        resultSet.getString("o"),
                        resultSet.getString("ecp")));
            }
            statement.close();
            con.close();
            return recepts;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<Pacient> checkLog(String polis, String pass) {
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            Statement statement = con.createStatement();
            String q = String.format("SELECT * FROM pacient WHERE polis = '%s' AND pass = '%s'",polis,pass);
            List<Pacient> pacients = new ArrayList<Pacient>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                pacients.add(new Pacient(resultSet.getInt("idpac"),
                        resultSet.getString("f"),
                        resultSet.getString("i"),
                        resultSet.getString("o"),
                        resultSet.getString("polis"),
                        resultSet.getString("pass"),
                        resultSet.getString("email")));
            }
            return pacients;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<Pacient> checkUnique(String polis) {
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            Statement statement = con.createStatement();
            String q = String.format("SELECT * FROM pacient WHERE polis ='%s' ",polis);
            List<Pacient> users = new ArrayList<Pacient>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                users.add(new Pacient(resultSet.getInt("idpac"),
                        resultSet.getString("f"),
                        resultSet.getString("i"),
                        resultSet.getString("o"),
                        resultSet.getString("polis"),
                        resultSet.getString("pass"),
                        resultSet.getString("email")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public void addP(Pacient pacient){
        try{
            Connection con= DriverManager.getConnection(conn_s,user_s,pass_s);
            PreparedStatement statement = con.prepareStatement("INSERT INTO pacient(f,i,o, polis, pass, email) " + "VALUES(?,?,?,?,?,?)");
            statement.setObject (1, pacient.f);
            statement.setObject (2, pacient.i);
            statement.setObject (3, pacient.o);
            statement.setObject (4, pacient.polis);
            statement.setObject (5, pacient.pass);
            statement.setObject (6, pacient.email);
            statement.execute();
        } catch (SQLException e){ e.printStackTrace();}
    }
    public String token() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        tokenList.add(array.toString());
        System.out.println(array.toString());
        return array.toString();
    }
    public String encr_decr(String text , boolean flag) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte raw[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        SecretKey secretkey = new SecretKeySpec(raw, "AES");
        String transformation = "AES/ECB/PKCS5Padding";
        Cipher cipher = Cipher.getInstance(transformation);
        if(flag== true){
            // Encrypt with key
            cipher.init(Cipher.ENCRYPT_MODE, secretkey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            String str = new String(encrypted, StandardCharsets.ISO_8859_1);
            return str;
        }
        else {
            // Decrypt with key
            cipher.init(Cipher.DECRYPT_MODE, secretkey);
            String result = new String(cipher.doFinal(text.getBytes(StandardCharsets.ISO_8859_1)));
            return result;
        }
    }

    public void PDFgen(String polis, int idrec ,boolean othcet, LocalDate startdate, LocalDate enddate) throws IOException, SAXException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, WriterException, ParseException {

        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Instant instant1 = startdate.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
        Date dates = Date.from(instant1);
        Instant instant2 = enddate.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
        Date datee = Date.from(instant2);
        List<Pacient> pacients = gPac(polis);
        List<Recept> recepts = new ArrayList();
        if(othcet==true) {
            recepts = gRec(pacients.get(0).id);
        }else {
            recepts = findRec(idrec);
        }
        int counter = 0;
        ListIterator<Recept> reclist = recepts.listIterator();
        String encoded = Base64.getEncoder().encodeToString(gQR(recepts.get(0)));
        ListIterator<Receptpreparat> preps = gPrep(idrec).listIterator();
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">" +
                "            <fo:layout-master-set>\n" +
                "                <fo:simple-page-master master-name=\"A4-portrail\" page-height=\"297mm\" page-width=\"210mm\" margin-top=\"5mm\" margin-bottom=\"5mm\" margin-left=\"5mm\" margin-right=\"5mm\">\n" +
                "                    <fo:region-body margin-top=\"5mm\" margin-bottom=\"20mm\"/>\n" +
                "                    <fo:region-before region-name=\"xsl-region-before\" extent=\"25mm\" display-align=\"before\" precedence=\"true\"/>\n" +
                "                </fo:simple-page-master>\n" +
                "            </fo:layout-master-set>\n" +
                "            <fo:page-sequence master-reference=\"A4-portrail\">\n" +
                "                <fo:flow flow-name=\"xsl-region-body\" border-collapse=\"collapse\" reference-orientation=\"0\">\n" );
        if(othcet==true){
            sb.append(
                    "                    <fo:block font-family=\"Tahoma\">Отчет от " +startdate+" до "+enddate+"</fo:block>\n" );
        }
        while (reclist.hasNext())
        {
            Recept recept = reclist.next();
            Date date = format2.parse(recept.dateo.substring(0,10));
            if((date.before(datee)&&date.after(dates)&&othcet==true)||othcet==false) {
                sb.append(
                        "                    <fo:block font-family=\"Tahoma\"> </fo:block>\n" +
                                "                    <fo:table table-layout=\"fixed\" width=\"100%\" font-size=\"10pt\" border-color=\"black\" border-width=\"0.4mm\"  border-style=\"solid\">\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(20)\"/>\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(45)\"/>\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(20)\"/>\n" +
                                "                        <fo:table-body>\n" +
                                "                            <fo:table-row>\n" +
                                "                                <fo:table-cell text-align=\"left\" display-align=\"center\" padding-left=\"2mm\">\n" +
                                "                                    <fo:block font-family=\"Tahoma\">\n" +
                                "                                       Лечащий врач:&#13;" + recept.f + " " + recept.i + " " + recept.o + " " + "" +
                                "                                    </fo:block>\n" +
                                "                                </fo:table-cell>\n" +
                                "                                <fo:table-cell text-align=\"center\" display-align=\"center\">\n" +
                                "                                    <fo:block font-family=\"Tahoma\" font-size=\"150%\">\n");

                if (othcet == true) {
                    counter++;
                    sb.append("Рецепт № " + counter);

                }
                if (othcet == false) {
                    sb.append("Поликлинника № " + counter);
                }
                sb.append(
                        "                                    </fo:block>\n" +
                                "                                    <fo:block space-before=\"3mm\"/>\n" +
                                "                                </fo:table-cell>\n" +
                                "                                <fo:table-cell text-align=\"right\" display-align=\"center\" padding-right=\"2mm\">\n" +
                                "                                    <fo:block font-family=\"Tahoma\">\n" +
                                "                                        Дата оформления: " + recept.dateo.substring(0, 10) + " " + "" +
                                "                                    </fo:block>\n" +
                                "                                    <fo:block font-family=\"Tahoma\" display-align=\"before\" space-before=\"6mm\">Страница <fo:page-number/> из <fo:page-number-citation ref-id=\"end-of-document\"/>\n" +
                                "                                    </fo:block>\n" +
                                "                                </fo:table-cell>\n" +
                                "                            </fo:table-row>\n" +
                                "                        </fo:table-body>\n" +
                                "                    </fo:table>\n" +
                                "                    <fo:table table-layout=\"fixed\" width=\"100%\" font-size=\"10pt\" border-color=\"black\" border-width=\"0.4mm\" border-style=\"solid\">\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(20)\"/>\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(45)\"/>\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(20)\"/>\n" +
                                "                        <fo:table-body>\n" +
                                "                            <fo:table-row>\n" +
                                "                                <fo:table-cell text-align=\"left\" display-align=\"center\" padding-left=\"2mm\">\n" +
                                "                                    <fo:block font-family=\"Tahoma\">\nПациент:&#13;" + pacients.get(0).f + " " + pacients.get(0).i + " " + pacients.get(0).o + " " + "" +
                                "                                    </fo:block>\n" +
                                "                                </fo:table-cell>\n" +
                                "                                <fo:table-cell text-align=\"center\" display-align=\"center\">\n" +
                                "                                    <fo:block font-family=\"Tahoma\" font-size=\"150%\">\n" +
                                "                                        Рецепт от " + recept.diagnoz + " " + "" +
                                "                                    </fo:block>\n" +
                                "                                    <fo:block space-before=\"3mm\"/>\n" +
                                "                                </fo:table-cell>\n" +
                                "                                <fo:table-cell text-align=\"right\" display-align=\"center\" padding-right=\"2mm\">\n" +
                                "                                    <fo:block font-family=\"Tahoma\">\n" +
                                "                                        Действует дней: " + recept.srok + " " + "" +
                                "                                    </fo:block>\n" +
                                "                                </fo:table-cell>\n" +
                                "                            </fo:table-row>\n" +
                                "                        </fo:table-body>\n" +
                                "                    </fo:table>\n" +
                                "                    <fo:block font-family=\"Tahoma\">Выписанные средства:</fo:block>\n" +
                                "                    <fo:table table-layout=\"fixed\" width=\"100%\" font-size=\"10pt\" border-color=\"black\" border-width=\"0.35mm\" border-style=\"solid\" text-align=\"center\" display-align=\"center\" space-after=\"5mm\" >\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(50)\"/>\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(10)\"/>\n" +
                                "                        <fo:table-column column-width=\"proportional-column-width(65)\"/>\n" +
                                "                        <fo:table-body font-size=\"95%\">\n" +
                                "                            <fo:table-row height=\"8mm\">\n" +
                                "                                <fo:table-cell>\n" +
                                "                                    <fo:block font-family=\"Tahoma\">Наименование</fo:block>\n" +
                                "                                </fo:table-cell>\n" +
                                "                                <fo:table-cell>\n" +
                                "                                    <fo:block font-family=\"Tahoma\">Кол-во</fo:block>\n" +
                                "                                </fo:table-cell>\n" +
                                "                                <fo:table-cell>\n" +
                                "                                    <fo:block font-family=\"Tahoma\">Способ применения</fo:block>\n" +
                                "                                </fo:table-cell>\n" +
                                "                            </fo:table-row>\n");
                while (preps.hasNext()) {
                    Receptpreparat receptpreparat = preps.next();
                    sb.append(
                            "                                <fo:table-row>\n" +
                                    "                                    <fo:table-cell>\n" +
                                    "                                        <fo:block font-family=\"Tahoma\">\n" +
                                    "                                           " + receptpreparat.prep_name + "\n" +
                                    "                                        </fo:block>\n" +
                                    "                                    </fo:table-cell>\n");
                    sb.append(
                            "                                    <fo:table-cell>\n" +
                                    "                                        <fo:block font-family=\"Tahoma\">\n" +
                                    "                                           " + receptpreparat.vipicano + " шт.\n" +
                                    "                                        </fo:block>\n" +
                                    "                                    </fo:table-cell>\n");

                    sb.append(
                            "                                    <fo:table-cell>\n" +
                                    "                                        <fo:block font-family=\"Tahoma\">\n" +
                                    "                                            Принимать по " + receptpreparat.doza + " " + receptpreparat.edizm + " " + receptpreparat.kolvodoz + " раз(а) в день в течении " + receptpreparat.kolvokurs + " " + receptpreparat.kurs + " \n" +
                                    "                                        </fo:block>\n" +
                                    "                                    </fo:table-cell>\n" +
                                    "                                </fo:table-row>\n");
                }
                sb.append(
                        "                        </fo:table-body>\n" +
                                "                    </fo:table>\n" +
                                "<fo:block font-family=\"Tahoma\"> </fo:block>\n");

            }
        }
        if(othcet==false){
        sb.append(
                        "                    <fo:block font-family=\"Tahoma\">Блок подтверждения:</fo:block>\n" +
                        "                    <fo:table table-layout=\"fixed\" width=\"100%\" font-size=\"10pt\" border-color=\"black\" border-width=\"0.35mm\" border-style=\"solid\" text-align=\"center\" display-align=\"center\" space-after=\"5mm\">\n" +
                        "                        <fo:table-column column-width=\"proportional-column-width(50)\"/>\n" +
                        "                        <fo:table-column column-width=\"proportional-column-width(75)\"/>\n" +
                        "                        <fo:table-body font-size=\"95%\">\n" +
                        "                            <fo:table-row height=\"40mm\">\n" +
                        "                                <fo:table-cell>\n" +
                        "                                    <fo:block font-family=\"Tahoma\">" +
                        "                                       <fo:external-graphic  src=\"url('data:image/png;base64,"+encoded+"')\" content-height=\"50mm\" content-width=\"50mm\"/>"+
                        "                                    </fo:block>\n" +
                        "                                </fo:table-cell>\n" +
                        "                            </fo:table-row>\n" +
                        "                        </fo:table-body>\n" +
                        "                    </fo:table>\n"+
                        "                    <fo:block id=\"end-of-document\">\n" +
                        "                    </fo:block>\n" );
        }
        if(othcet==true){
            sb.append(
                    "                    <fo:block font-family=\"Tahoma\">Итог:</fo:block>\n" +
                            "                    <fo:table table-layout=\"fixed\" width=\"100%\" font-size=\"10pt\" border-color=\"black\" border-width=\"0.35mm\" border-style=\"solid\" text-align=\"center\" display-align=\"center\" space-after=\"5mm\">\n" +
                            "                        <fo:table-column column-width=\"proportional-column-width(50)\"/>\n" +
                            "                        <fo:table-column column-width=\"proportional-column-width(75)\"/>\n" +
                            "                        <fo:table-body font-size=\"95%\">\n" +
                            "                            <fo:table-row height=\"8mm\">\n" +
                            "                                <fo:table-cell>\n" +
                            "                                    <fo:block font-family=\"Tahoma\"> Рецептов виписано: " + counter +
                            "                                    </fo:block>\n" +
                            "                                </fo:table-cell>\n" +
                            "                            </fo:table-row>\n" +
                            "                        </fo:table-body>\n" +
                            "                    </fo:table>\n"+
                            "                    <fo:block id=\"end-of-document\">\n" +
                            "                    </fo:block>\n" );
        }
        sb.append(
                        "                </fo:flow>\n" +
                        "            </fo:page-sequence>\n" +
                        "        </fo:root>\n");
        FopFactory fopFactory = FopFactory.newInstance(new File("src/main/resources/fop.xconf"));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        OutputStream out = new BufferedOutputStream(new FileOutputStream("output.pdf"));
        try {
            Fop fop = fopFactory.newFop("application/pdf",out);
            Transformer transformer = transformerFactory.newTransformer();
            Source source = new StreamSource(new ByteArrayInputStream(sb.toString().getBytes("UTF8")));
            Result result = new SAXResult(fop.getDefaultHandler());
            transformer.transform(source,result);
        }catch (Exception e){
            System.err.println("Error gen PDF " + e.getLocalizedMessage());
        }finally {
            out.close();
        }
    }

}
