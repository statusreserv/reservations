package com.statusreserv.reservations.constants;

public final class Endpoints {
    private Endpoints() {}

    public static final String API = "/api";
    public static final String ID = "/{id}";

    public static final String SERVICES = API + "/services";
    public static final String SCHEDULE = API + "/schedules";
    public static final String AVAILABILITY = API + "/availabilities";
    public static final String AUTH = API + "/auth";

    public static final String LOGIN = "/login";
}
