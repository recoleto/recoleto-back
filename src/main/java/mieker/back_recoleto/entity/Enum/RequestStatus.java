package mieker.back_recoleto.entity.Enum;

public enum RequestStatus {
    PENDENTE, // o usuario solicitou
    REPROVADO, // a empresa recusou a solicitação
    APROVADO, // a empresa aceitou e o usuário tem que levar o residuo
    CANCELADO, // se o usuário n levar
    RECEBIDO // o usuario levou e dai recebe os pontos
}
