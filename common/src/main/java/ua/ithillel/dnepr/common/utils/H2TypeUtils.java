package ua.ithillel.dnepr.common.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class H2TypeUtils {
    /**
     * Encapsulates H2 data types
     * https://h2database.com/html/datatypes.html#boolean_type
     */
    public enum H2Types {
        /**
         * Possible values: -2147483648 to 2147483647.
         * See also integer literal grammar. Mapped to java.lang.Integer.
         */
        INT(Integer.class, "INTEGER", "INT", "INTEGER", "MEDIUMINT", "INT4", "SIGNED"),
        BOOLEAN(Boolean.class, "BOOLEAN", "BOOLEAN", "BIT", "BOOL");

        private final List<String> typeAliases = new ArrayList<>();
        private final Class<?> javaType;
        private final String defaultType;

        H2Types(Class<?> javaType, String defaultType, String... typeAliases) {
            this.javaType = javaType;
            this.defaultType = defaultType;
            this.typeAliases.addAll(Arrays.asList(typeAliases));
        }

        public Class<?> getJavaType() {
            return this.javaType;
        }

        public static Class<?> toJavaType(String h2DataTypeName) {
            Class<?> result = null;
            for (H2Types h2Type : H2Types.values()) {
                for (String h2TypeAlias : h2Type.typeAliases) {
                    if (h2TypeAlias.equalsIgnoreCase(h2DataTypeName)) {
                        result = h2Type.javaType;
                        break;
                    }
                }
            }
            return result;
        }

        public static String toH2Type(Class<?> javaType) {
            String result = null;
            for (H2Types h2Type : H2Types.values()) {
                if (h2Type.javaType == javaType) {
                    result = h2Type.defaultType;
                    break;
                }
            }
            return result;
        }
    }
}
