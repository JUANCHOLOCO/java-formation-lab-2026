package com.indra.transporte;

import java.util.ArrayList;
import java.util.List;

import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Horario;

import lombok.Data;

@Data
public class ProgramadorRutas {

    List<Horario> horarios = new ArrayList<>();

    public void programar(Horario horario) {
        validarCamposObligatorios(horario);
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        horarios.add(horario);
    }

    public boolean debeValidarTipoRutasYBuses(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        String tipoBus = horario.getBus().getTipo();
        String tipoRuta = horario.getRuta().getTipo();

        if ("Electric".equals(tipoBus) && !"Electric".equals(tipoRuta)) {
            throw new IllegalArgumentException("Los buses eléctricos solo pueden ir a rutas eléctricas");
        }
        return true;
    }

    private void validarCamposObligatorios(Horario horario) {
    if (horario == null) {
        throw new IllegalArgumentException("El horario no puede ser nulo");
    }
    validarBus(horario.getBus());
    if (horario.getRuta() == null) {
        throw new IllegalArgumentException("La ruta no puede ser nula");
    }
    if (estaVacio(horario.getRuta().getTipo())) {
        throw new IllegalArgumentException("El tipo de ruta no puede ser nulo o vacío");
    }
    if (horario.getHoraSalida() == null || horario.getHoraLlegada() == null) {
        throw new IllegalArgumentException("Las horas de salida y llegada son obligatorias");
    }
}

private void validarBus(Bus bus) {
    if (bus == null) {
        throw new IllegalArgumentException("El bus no puede ser nulo");
    }
    if (estaVacio(bus.getPlaca())) {
        throw new IllegalArgumentException("La placa del bus no puede ser nula o vacía");
    }
    if (estaVacio(bus.getTipo())) {
        throw new IllegalArgumentException("El tipo de bus no puede ser nulo o vacío");
    }
}

private static boolean estaVacio(String valor) {
    return valor == null || valor.isBlank();
}

}
