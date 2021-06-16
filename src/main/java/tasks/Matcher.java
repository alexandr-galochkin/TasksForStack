package tasks;

class Matcher {
    static boolean match(String string, String pattern) throws Exception {
        if (string.length()!= pattern.length()){
            return false;
        }
        for (int i = 0; i < string.length(); i++){
            char currentType = pattern.charAt(i);
            char currentChar = string.charAt(i);
            switch (currentType){
                case 'a':
                    if (!isLetter(currentChar)){
                        return false;
                    }
                    break;
                case 'd':
                    if (!isNumber(currentChar)){
                        return false;
                    }
                    break;
                case ' ':
                    if (currentChar != ' '){
                        return false;
                    }
                    break;
                case '*':
                    if ((!isNumber(currentChar)) && (!isLetter(currentChar))){
                        return false;
                    }
                    break;
                default:
                    throw new Exception();
            }
        }
        return true;
    }

    private static boolean isNumber(char c){
        return ((c >= '0') && (c <= '9'));
    }

    private static boolean isLetter(char c){
        return ((c >= 'a') && (c <= 'z'));
    }

    static void testMatch(){
        TestRunner runner = new TestRunner("match");

        runner.expectFalse(() -> match("xy", "a"));
        runner.expectFalse(() -> match("x", "d"));
        runner.expectFalse(() -> match("0", "a"));
        runner.expectFalse(() -> match("*", " "));
        runner.expectFalse(() -> match(" ", "a"));

        runner.expectTrue(() -> match("01 xy", "dd aa"));
        runner.expectTrue(() -> match("1x", "**"));

        runner.expectException(() -> {
            match("x", "w");
        });
    }
}
