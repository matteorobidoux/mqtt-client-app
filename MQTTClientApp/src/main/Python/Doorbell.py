#!/usr/bin/env python3
#############################################################################
# Filename    : Doorbell.py
# Description :	activate buzzer sound on button push
# Author      : freenove
# modification: 2022/11/16
########################################################################
import RPi.GPIO as GPIO
buzzerPin = 11 # define buzzerPin
buttonPin = 40 # define buttonPin

def setup():
    GPIO.setmode(GPIO.BOARD) # use PHYSICAL GPIO Numbering
    GPIO.setup(buzzerPin, GPIO.OUT) # set buzzerPin to OUTPUT mode
    GPIO.setup(buttonPin, GPIO.IN, pull_up_down=GPIO.PUD_UP) # set buttonPin to PULL UP INPUT mode

def loop():
    if GPIO.input(buttonPin)==GPIO.LOW: # if button is pressed
        GPIO.output(buzzerPin,GPIO.HIGH) # turn on buzzer
        print ('on')
    else : # if button is relessed
        GPIO.output(buzzerPin,GPIO.LOW) # turn off buzzer
        print ('off')

def destroy():
    GPIO.cleanup() # Release all GPIO

if __name__ == '__main__': # Program entrance
    setup()
    try:
        loop()
    except KeyboardInterrupt: # Press ctrl-c to end the program.
        destroy()

