#include <StaticThreadController.h>
#include <Thread.h>
#include <ThreadController.h>

#include <Adafruit_NeoPixel.h>

#define VERTICAL_PIN 4 
#define VERTICAL_LED_COUNT 60
Adafruit_NeoPixel verticalStrip = Adafruit_NeoPixel(VERTICAL_LED_COUNT, VERTICAL_PIN, NEO_GRB + NEO_KHZ800);

#define ARM_PIN 7
#define ARM_LED_COUNT 30
Adafruit_NeoPixel armStrip = Adafruit_NeoPixel(ARM_LED_COUNT, ARM_PIN, NEO_GRB + NEO_KHZ800);

#define CARGO_PIN 8
#define CARGO_LED_COUNT 60
Adafruit_NeoPixel cargoStrip = Adafruit_NeoPixel(CARGO_LED_COUNT, CARGO_PIN, NEO_GRB + NEO_KHZ800);

//Used for rainbow cycle
struct RGB
{
  unsigned char R;
  unsigned char G;
  unsigned char B;
};
struct RGB rgb;

//Constants
int vPulseDel = 40;
int vChaseDel = 40;
int aFadeDel = 30;
int chaseLength = 5;
int aDoubleChaseDel = 20;
int cDoubleChaseSize = 5;
int cDoubleChaseDel = 20;
boolean doubleChaseOrPulse = false; //false = pulse, true = doublecChase

//Variables
int x = 0;
int y = 0;
int z = 0;
int h = VERTICAL_LED_COUNT;
int k = 0;
int q = 0;
int f = CARGO_LED_COUNT;
int n = 0;
boolean yAtMax = false;
boolean upComplete = false;
boolean chaseAtTop = false;

//Threads
Thread verticalPulseFPBlue;
Thread verticalPulseBPBlue;
Thread verticalPulseFPRed;
Thread verticalPulseBPRed;
Thread armFadeRed;
Thread armFadeBlue;
Thread verticalDoubleChaseRed;
Thread verticalDoubleChaseBlue;
Thread verticalChaseRed;
Thread verticalChaseBlue;
Thread cargoDoubleChaseRed;
Thread cargoDoubleChaseBlue;

StaticThreadController<6> redControl (&verticalPulseFPRed,&verticalPulseBPRed,&armFadeRed,&verticalDoubleChaseRed,&verticalChaseRed,&cargoDoubleChaseRed);
StaticThreadController<6> blueControl (&verticalPulseFPBlue,&verticalPulseBPBlue,&armFadeBlue,&verticalDoubleChaseBlue,&verticalChaseBlue,&cargoDoubleChaseBlue);

void setup() {
  verticalStrip.begin();
  verticalStrip.show();
  armStrip.begin();
  armStrip.show();
  cargoStrip.begin();
  cargoStrip.show();

  verticalPulseFPBlue = Thread(vPulseFPBlue);
  verticalPulseBPBlue = Thread(vPulseBPBlue);
  verticalPulseFPRed = Thread(vPulseFPRed);
  verticalPulseBPRed = Thread(vPulseBPRed);
  armFadeRed = Thread(aFadeRed);
  armFadeBlue = Thread(aFadeBlue);
  verticalDoubleChaseRed = Thread(vDoubleChaseRed);
  verticalDoubleChaseBlue = Thread(vDoubleChaseBlue);
  verticalChaseRed = Thread(vChaseRed);
  verticalChaseBlue = Thread(vChaseBlue);
  cargoDoubleChaseRed = Thread(cDoubleChaseRed);
  cargoDoubleChaseBlue = Thread(cDoubleChaseBlue);
  
  
  verticalPulseFPBlue.setInterval(vPulseDel);
  verticalPulseBPBlue.setInterval(vPulseDel);
  verticalPulseFPRed.setInterval(vPulseDel);
  verticalPulseBPRed.setInterval(vPulseDel);
  armFadeRed.setInterval(aFadeDel);
  armFadeBlue.setInterval(aFadeDel);
  verticalDoubleChaseRed.setInterval(aDoubleChaseDel);
  verticalDoubleChaseBlue.setInterval(aDoubleChaseDel);
  verticalChaseRed.setInterval(vChaseDel);
  verticalChaseBlue.setInterval(vChaseDel);
  cargoDoubleChaseRed.setInterval(cDoubleChaseDel);
  cargoDoubleChaseBlue.setInterval(cDoubleChaseDel);

  verticalPulseBPBlue.enabled = false;
  verticalPulseBPRed.enabled = false;
  
//  verticalPulseFPRed.enabled = false;
//  verticalPulseFPBlue.enabled = false;
  verticalChaseRed.enabled = false;
  verticalChaseBlue.enabled = false;
  verticalDoubleChaseRed.enabled = false;
  verticalDoubleChaseBlue.enabled = false;
  

  verticalStrip.setBrightness(100);
  cargoStrip.setBrightness(100);
  pinMode(11, INPUT);
  pinMode(2,INPUT);

}

void loop() {
  if (digitalRead(2) == HIGH) {
    setAll(0,255,0,0);
    setAll(0,255,0,1);
    setAll(0,255,0,2);
  } else {
    if (digitalRead(11) == HIGH) {
      redControl.run();
    } else {
      blueControl.run();   
      //cycleAllColors();
    }
  }
}

void vDoubleChaseRed() {
  vDoubleChase(255,0,0,chaseLength);
}

void vDoubleChaseBlue() {
  vDoubleChase(0,0,255,chaseLength);
}

void vPulseFPBlue() {
  vPulseFP(0,0,255);
}

void vPulseFPRed() {
  vPulseFP(255,0,0);
}

void vPulseBPBlue() {
  vPulseBP(0,0,255);
}

void vPulseBPRed() {
  vPulseBP(255,0,0);
}

void aFadeRed() {
  aFade(255,0,0,1);
}

void aFadeBlue() {
  aFade(0,0,255,1);
}

void vChaseRed() {
  vChase(255,0,0,chaseLength);
}

void vChaseBlue() {
  vChase(0,0,255,chaseLength);
}

void cDoubleChaseRed() {
  cDoubleChase(255,0,0,cDoubleChaseSize);
}

void cDoubleChaseBlue() {
  cDoubleChase(0,0,255,cDoubleChaseSize);
}

void vPulseFP(int red, int green, int blue) {
    if (x<=VERTICAL_LED_COUNT) {
      verticalStrip.setPixelColor(x-4
      ,red-225>0 ? red-225:red=0
      ,green-225>0 ? green-225:green=0
      ,blue-225>0 ? blue-225:blue=0);
      
      verticalStrip.setPixelColor(x-3   
      ,red-163>0 ? red-163:red=0
      ,green-163>0 ? green-163:green=0
      ,blue-163>0 ? blue-163:blue=0);
     
      verticalStrip.setPixelColor(x-2    
      ,red-81>0 ? red-81:red=0
      ,green-81>0 ? green-81:green=0
      ,blue-81>0 ? blue-81:blue=0);
      
      verticalStrip.setPixelColor(x-1,red,green,blue);
      verticalStrip.setPixelColor(x,red,green,blue);
      verticalStrip.setPixelColor(x+1,red,green,blue);
      
      verticalStrip.setPixelColor(x+2 
      ,red-81>0 ? red-81:red=0
      ,green-81>0 ? green-81:green=0
      ,blue-81>0 ? blue-81:blue=0);
      
      verticalStrip.setPixelColor(x+3 
      ,red-163>0 ? red-163:red=0
      ,green-163>0 ? green-163:green=0
      ,blue-163>0 ? blue-163:blue=0);
     
      verticalStrip.setPixelColor(x+4    
      ,red-225>0 ? red-225:red=0
      ,green-225>0 ? green-225:green=0
      ,blue-225>0 ? blue-225:blue=0);
     
      verticalStrip.show();
      x = x+1;
    } else {
      verticalPulseFPBlue.enabled = false;
      verticalPulseBPBlue.enabled = true;
      verticalPulseFPRed.enabled = false;
      verticalPulseBPRed.enabled = true;
    }
}


void vPulseBP(int red, int green, int blue) {
    if (x>=0) {
     verticalStrip.setPixelColor(x-4
      ,red-225>0 ? red-225:red=0
      ,green-225>0 ? green-225:green=0
      ,blue-225>0 ? blue-225:blue=0);
      
      verticalStrip.setPixelColor(x-3   
      ,red-163>0 ? red-163:red=0
      ,green-163>0 ? green-163:green=0
      ,blue-163>0 ? blue-163:blue=0);
     
      verticalStrip.setPixelColor(x-2    
      ,red-81>0 ? red-81:red=0
      ,green-81>0 ? green-81:green=0
      ,blue-81>0 ? blue-81:blue=0);
      
      verticalStrip.setPixelColor(x-1,red,green,blue);
      verticalStrip.setPixelColor(x,red,green,blue);
      verticalStrip.setPixelColor(x+1,red,green,blue);
      
      verticalStrip.setPixelColor(x+2 
      ,red-81>0 ? red-81:red=0
      ,green-81>0 ? green-81:green=0
      ,blue-81>0 ? blue-81:blue=0);
      
      verticalStrip.setPixelColor(x+3 
      ,red-163>0 ? red-163:red=0
      ,green-163>0 ? green-163:green=0
      ,blue-163>0 ? blue-163:blue=0);
     
      verticalStrip.setPixelColor(x+4    
      ,red-225>0 ? red-225:red=0
      ,green-225>0 ? green-225:green=0
      ,blue-225>0 ? blue-225:blue=0);

      verticalStrip.show();
      x = x-1;
    } else {
      verticalPulseFPBlue.enabled = true;
      verticalPulseBPBlue.enabled = false;
      verticalPulseFPRed.enabled = true;
      verticalPulseBPRed.enabled = false;
    }
}

void vDoubleChase (int red, int green, int blue, int chaseSize) {
    if (z <= VERTICAL_LED_COUNT) {
      verticalStrip.setPixelColor(z-chaseSize,0,0,0);
      verticalStrip.setPixelColor(z,red,green,blue);
      z = z+1;
      if (z == VERTICAL_LED_COUNT) {
        z=0;
      }
    } 
    if (h>=0) {
      verticalStrip.setPixelColor(h+chaseSize,0,0,0);
      verticalStrip.setPixelColor(h,red,green,blue);
      h = h-1;
      if (h == 0) {
        h = VERTICAL_LED_COUNT;
      }
    }
          verticalStrip.show();
}

void cDoubleChase (int red, int green, int blue, int chaseSize) {
    if (q <= CARGO_LED_COUNT) {
      cargoStrip.setPixelColor(q-chaseSize,0,0,0);
      cargoStrip.setPixelColor(q,red,green,blue);
      q = q+1;
      if (q == CARGO_LED_COUNT) {
        q=0;
      }
    } 
    if (f>=0) {
      cargoStrip.setPixelColor(f+chaseSize,0,0,0);
      cargoStrip.setPixelColor(f,red,green,blue);
      f = f-1;
      if (f == 0) {
        f = CARGO_LED_COUNT;
      }
    }
          cargoStrip.show();
}

void aFade (int red, int green, int blue, int stripN) {
  if (y<=100 && !yAtMax) {
    armStrip.setBrightness(y);
    armStrip.show();
    setAll(red, green, blue, stripN);
    y = y+1;
    if (y == 100) {
      yAtMax = true;
    }
  } else if (y>=0 && yAtMax) {
    armStrip.setBrightness(y);
    armStrip.show();
    setAll(red, green, blue, stripN);
    y = y-1;
    if (y == 0) {
      yAtMax = false;
    }
  }
}


void vChase(int red, int green, int blue, int chaseSize) {
    if (k <= VERTICAL_LED_COUNT && !chaseAtTop) {
      verticalStrip.setPixelColor(k-chaseSize,0,0,0);
      verticalStrip.setPixelColor(k,red,green,blue);
      k = k+1;
      if (k == VERTICAL_LED_COUNT) {
        chaseAtTop = true;
      }
    }
    if (k >= 0 && chaseAtTop) {
      verticalStrip.setPixelColor(k+chaseSize,0,0,0);
      verticalStrip.setPixelColor(k,red,green,blue);
      k = k-1;
      if (k == 0) {
        chaseAtTop = false;
      }
    }
}




void cycleAllColors() {
  if (n<=360)
    HSVtoRGB(n,1,1);
    setAll(rgb.R,rgb.G,rgb.B,0);
    setAll(rgb.R,rgb.G,rgb.B,1);
    setAll(rgb.R,rgb.G,rgb.B,2);
    n = n+1;
    if (n == 360) {
      n = 0;
    }
}























void setAll(int red, int green, int blue, int stripN) {
  if (stripN == 0) {
    for (int x = 0; x<=VERTICAL_LED_COUNT; x++) {
      verticalStrip.setPixelColor(x, red, green, blue);
    }
    verticalStrip.show();
  } else if (stripN == 1) {
    for (int x = 0; x<=ARM_LED_COUNT; x++) {
      armStrip.setPixelColor(x, red, green, blue);
    }
    armStrip.show();
  } else if (stripN == 2) {
    for (int x = 0; x<=CARGO_LED_COUNT; x++) {
      cargoStrip.setPixelColor(x, red, green, blue);
    }
    cargoStrip.show();
  }
}

void HSVtoRGB(double h, double s, double v){
  
  double r = 0, g = 0, b = 0;

  if (s == 0)
  {
    r = v;
    g = v;
    b = v;
  }
  else
  {
    int i;
    double f, p, q, t;

    if (h == 360)
      h = 0;
    else
      h = h / 60;

    i = (int)trunc(h);
    f = h - i;

    p = v * (1.0 - s);
    q = v * (1.0 - (s * f));
    t = v * (1.0 - (s * (1.0 - f)));

    switch (i)
    {
    case 0:
      r = v;
      g = t;
      b = p;
      break;

    case 1:
      r = q;
      g = v;
      b = p;
      break;

    case 2:
      r = p;
      g = v;
      b = t;
      break;

    case 3:
      r = p;
      g = q;
      b = v;
      break;

    case 4:
      r = t;
      g = p;
      b = v;
      break;

    default:
      r = v;
      g = p;
      b = q;
      break;
    }

  }


  rgb.R = r * 255;
  rgb.G = g * 255;
  rgb.B = b * 255;

  return rgb;
}
