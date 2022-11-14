#!/usr/bin/env python3
#############################################################################
# Filename    : SenseLED.py
# Description :	detect motion to trigger LED
# Author      : freenove
# modification: 2022/11/12
########################################################################
import RPi.GPIO as GPIO
ledPin = 12 # define ledPin
sensorPin = 13 # define sensorPin

def setup():
    GPIO.setmode(GPIO.BOARD) # use PHYSICAL GPIO Numbering
    GPIO.setup(ledPin, GPIO.OUT) # set ledPin to OUTPUT mode
    GPIO.setup(sensorPin, GPIO.IN) # set sensorPin to INPUT mode
def loop():
    while True:
        if GPIO.input(sensorPin)==GPIO.HIGH:
            GPIO.output(ledPin,GPIO.HIGH) # turn on led
            print ('led turned on >>>')
        else :
            GPIO.output(ledPin,GPIO.LOW) # turn off led
            print ('led turned off <<<')

def destroy():
    GPIO.cleanup() # Release GPIO resource

if __name__ == '__main__': # Program entrance
    print ('Program is starting...')
    setup()
    try:
        loop()
    except KeyboardInterrupt: # Press ctrl-c to end the program. 
        destroy()

