package ua.ithillel.dnepr.roman.gizatulin.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.ithillel.dnepr.common.Calc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Program03Test {
    @InjectMocks
    private Program03 program03 = new Program03();
      @Mock
    private Calc calc;

    @Test
    void addAndSubtract() {
        when(calc.add(anyDouble(), anyDouble())).thenReturn(15.0);
        when(calc.subtract(anyDouble(), anyDouble())).thenReturn(5.0);
        assertEquals(5, program03.addAndSubtract(123, 123));
    }
}