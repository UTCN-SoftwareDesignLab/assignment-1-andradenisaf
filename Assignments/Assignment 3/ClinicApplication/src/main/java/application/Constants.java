package application;

import java.util.*;

import static application.Constants.Rights.*;
import static application.Constants.Roles.*;

public class Constants {

    public static class Roles {
        public static final String ADMINISTRATOR = "administrator";
        public static final String SECRETARY = "secretary";
        public static final String DOCTOR = "doctor";

        public static final String[] ROLES = new String[]{ADMINISTRATOR, SECRETARY, DOCTOR};
    }

    public static class Rights {
        public static final String CREATE_USER = "create_user";
        public static final String READ_USER = "read_user";
        public static final String UPDATE_USER = "update_user";
        public static final String DELETE_USER = "delete_user";

        public static final String CREATE_PATIENT = "create_patient";
        public static final String UPDATE_PATIENT = "update_patient";

        public static final String CREATE_CONSULTATION = "create_consultation";
        public static final String READ_CONSULTATION = "read_consultation";
        public static final String UPDATE_CONSULTATION = "update_consultation";
        public static final String DELETE_CONSULTATION = "delete_consultation";


        public static final String[] RIGHTS = new String[]{CREATE_USER, READ_USER, UPDATE_USER, DELETE_USER, CREATE_PATIENT, UPDATE_PATIENT, CREATE_CONSULTATION, READ_CONSULTATION, UPDATE_CONSULTATION, DELETE_CONSULTATION};
    }

    public static Map<String, List<String>> getRolesRights() {
        Map<String, List<String>> ROLES_RIGHTS = new HashMap<>();
        for (String role : ROLES) {
            ROLES_RIGHTS.put(role, new ArrayList<>());
        }
        ROLES_RIGHTS.get(ADMINISTRATOR).addAll(Arrays.asList(CREATE_USER, READ_USER, UPDATE_USER, DELETE_USER));

        ROLES_RIGHTS.get(SECRETARY).addAll(Arrays.asList(CREATE_PATIENT, UPDATE_PATIENT, CREATE_CONSULTATION, READ_CONSULTATION, UPDATE_CONSULTATION, DELETE_CONSULTATION));

        ROLES_RIGHTS.get(DOCTOR).addAll(Arrays.asList(UPDATE_CONSULTATION, READ_CONSULTATION));

        return ROLES_RIGHTS;
    }

}
