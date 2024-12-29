package mieker.back_recoleto.entity.Enum;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public enum Role {
    ADMIN,
    USUARIO,
    EMPRESA,
    PONTO_DE_COLETA
}
