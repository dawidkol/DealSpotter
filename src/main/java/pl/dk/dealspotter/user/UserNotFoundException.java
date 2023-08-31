package pl.dk.dealspotter.user;

class UserNotFoundException extends RuntimeException{
    private static final String MESSAGE = "Username not exists in DB";
    public UserNotFoundException() {
        super(MESSAGE);
    }
}
