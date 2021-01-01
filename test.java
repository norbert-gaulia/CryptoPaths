public class test {
   public static void main(String[] args) {
       System.out.println(roundPrice("49.464"));
   } 
       public static String round(String d) {
        String s = d, s2 = "";
        int places = 0;
        boolean right = false;
        for (int i = 0; i < s.length(); i++) {
            if (places >= 2 && s.charAt(i - 1) != '0')
                break;
            s2 += s.charAt(i);
            if (right)
                places++;
            if (s.charAt(i) == '.') {
                right = true;
                if (s.charAt(i + 1) != '0') {
                    s2 += s.charAt(i + 1);
                    break;
                }
            }
        }
        return s2;
    }
    
    public static String roundPrice(String d) {
        String s = d, s2 = "";
        int places = 0;
        boolean right = false;
        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.charAt(i));
            if (places >= 2 && s.charAt(i - 1) != '0')
                break;
            s2 += s.charAt(i);
            if (right)
                places++;
            if (s.charAt(i) == '.') {
                right = true;
            }
        }
        return s2;
    }
}
