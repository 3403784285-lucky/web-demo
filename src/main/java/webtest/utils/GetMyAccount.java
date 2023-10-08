package webtest.utils;
public class GetMyAccount {
    private static String  account;
    public static String getMyAccount() {
        System.out.println("给爷试试");
            String array = new SnowFlakeId(0).nextId() + "";
            account = array.substring(0, 10);
            System.out.println(account);
        return account;

    }

}
