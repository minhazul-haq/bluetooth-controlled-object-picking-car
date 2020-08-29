#include <avr/io.h>
#include <avr/interrupt.h>
#include <util/delay.h>

#define BAUD_PRESCALE 3
#define F_CPU 1000000UL

int isKeyReleased;

void gear_forward()
{
	PORTB = 0b01000010;
}

void gear_backward()
{
	PORTB = 0b00100001;
}

void gear_left()
{
	PORTB = 0b01000001;
}

void gear_right()
{
	PORTB = 0b00100010;
}

void keyReleased()
{
	isKeyReleased = 1;
	PORTB = 0x00;
}

void servo_expand()
{
	if (isKeyReleased==0) 
	{
		if (OCR1A<2350) OCR1A += 100;
		_delay_ms(50);
	}
}

void servo_compress()
{
	if (isKeyReleased==0) 
	{
		if (OCR1A>830) OCR1A -= 100;
		_delay_ms(50);
	}
}

//Serial com. Interrupt Service Routine (runs each time a byte is received)
ISR(USART_RXC_vect)
{
	char dat = UDR;
		
	switch (dat) //Which ASCII character was received?
    {
		//up key
		case 128:   
			gear_forward();
			PORTA = 0x00;
			break;
        //left key
		case 130: 
			gear_left();
			PORTA = 0x00;
			break;
        //down key
		case 140:  
			gear_backward();
            PORTA = 0x00;
			break;
        //right key
		case 142:  
			gear_right();
			PORTA = 0x00;
			break;
        //expand key
		case 2:
			isKeyReleased = 0;
			servo_expand(); 
			PORTA = 0xFF; 
			break;
        //compress key
		case 12:  
			isKeyReleased = 0;
			servo_compress();
			PORTA = 0xFF;
		    break;
		//key released
		case 0:
			keyReleased();
			PORTA = 0x00;
			break;
		//others
		default:   
			PORTA = 0x00;
	}
}

 
int main(void)
{
	//I/O Initialization
    DDRA = 0xFF;
	DDRB = 0xFF;
	
	PORTA = 0;
    PORTB = 0;
 
    //UART initialization
    UCSRB |= (1 << RXEN) | (1 << TXEN); //enable Tx and Rx
    UCSRC |= (1 << UCSZ0) | (1 << UCSZ1); //set size to 8 bits
    UBRRL = BAUD_PRESCALE;        
    UBRRH = (BAUD_PRESCALE >> 8); //set baudrate registers
	UCSRB |= (1 << RXCIE); //enable USART-interrupt
	
	//PWM initialization	
	DDRD = 0xFF; // OC1A output
	TCCR1A |= (1<<WGM11) | (1<<COM1A1); // clear OC1A on compare
	TCCR1B |= (1<<WGM12) | (1<<WGM13); // fast pwm, mode 14
	TCCR1B |= (1<<CS10); // prescale=8, step=1us
	ICR1 = 20000; // 50 Hz
	
	//set initial OCR1A value
	OCR1A = 2350;
	
	sei(); //Enable global interrupt
 
    while(1) //infinite loop
	{
		//do nothing
    }	

    return 0;
}