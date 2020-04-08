# 1) laatste python version: https://www.python.org/ (ook pip wordt dan geinstallerd)
     # 32 bit
     # NB eventueel: You should consider upgrading pip via the 'python -m pip install --upgrade pip' 

# 2) Thonny zonder python installeren (Thonny is ook te downloade/installeren met Python, maar dat wordt ingewikkeld bij updates, pip, pyinstaller etc)
     # "pip install thonnyapp"

# 3) pyinstaller (http://www.pyinstaller.org/)
     # installeren via "pip install pyinstaller"
     #
     #
     # met pyinstaller exe aanmaken:  Go to your .py program’s directory and run in cmd console: 'pyinstaller yourprogram.py'
     # bv: 'pyinstaller --onefile email_tbw.py'
     #
     # NB indien "failed to create process"-> "pip uninstall pyinstaller" en dan weer "pip install pyinstaller"
     #
     # NB All “module not found” messages are written to the build/name/warnname.txt file.
     #    They are not displayed to standard output because there are many of them. Examine the warning file; often there will be dozens of modules not found, but their absence has no effect.
     #    When you run the bundled app and it terminates with an ImportError, that is the time to examine the warning file.
     #    Then see Helping PyInstaller Find Modules below for how to proceed.(https://pythonhosted.org/PyInstaller/when-things-go-wrong.html)
     #
     # run de aangemaakte exe from command line: PS C:\NetBeansProjects\python\dist> .\email_5.exe 1 2 3 4

# 5) check all installed python packages: (>>> help('modules'))
     #nb deze geeft dus niet aan waar Thonny bij kan komen (andere folder)

# NB .sendmail(), .send_message and .starttls() will implicitly call .ehlo_or_helo_if_needed()
#    therefore there is no need to explicitly call it again.
#    source: https://stackoverflow.com/questions/33857698/sending-email-from-python-using-starttls


# NB https://stackabuse.com/how-to-send-emails-with-gmail-using-python/
# NB http://www.pybloggers.com/2018/12/sending-emails-with-python/



# NB turn on " Allow apps that use less secure sign-in" (via login Yahoo web site)
#             otherwise 'SMTPServerDisconnected' exception :
#             "This exception is raised when the server unexpectedly disconnects, or when an attempt is made to use the SMTP instance before connecting it to a server."
# NB or generate app password (outlook desktop / outlook android?) (via login Yahoo/GMail web site)


import os
import sys
import datetime

# Import smtplib for the actual sending function
import smtplib, ssl

# Import the email modules we'll need
# The central class in the email package is the EmailMessage class, imported from the email.message module
from email.message import EmailMessage 


# printing general info
print ("[", datetime.datetime.utcnow().strftime("%d %b %Y %H:%M:%S %Z"),"UTC", "]")
print ("TurboWin+ python email module (build 16-September-2019 10:00 UTC)")
print ("Script name: ", sys.argv[0])
print ("Number of arguments: ", len(sys.argv))
#print ("The arguments are: " , str(sys.argv))

#verify number of arguments
required_num_arguments = 11
if (len(sys.argv) != required_num_arguments):
    print ("invalid number of arguments (must be", required_num_arguments,")")
    sys.exit(1)

#read arguments
# NB module name (file name) = sys.argv[0]
smtp_mode             = sys.argv[1]       # eg SMTP_HOST_SHIP, GMAIL_TLS, GMAIL_SSL
smtp_host_local       = sys.argv[2]       # eg 'smtp.gmail.com' or 'smtp.knmi.nl'
smtp_password_local   = sys.argv[3]       # eg 'P@ssword!' or 'null'
send_to               = sys.argv[4]       # 'martin.stam@home.nl'
send_from             = sys.argv[5]       # 'turbowin.observations@gmail.com'
subject               = sys.argv[6]       #'dit is een test via ' + smtp_mode
body                  = sys.argv[7]       # "This is an obs test body line"
send_cc               = sys.argv[8]       # "çc email address"
smtp_port_local       = sys.argv[9]       # only used in case of SMTP_HOST_SHIP (eg 'null'in case of Gmail)
attachment            = sys.argv[10]      # 'none' / 'yes'

print ("The arguments are:")
print("smtp_mode           = ", smtp_mode)
print("smtp_host_local     = ", smtp_host_local)
print("smtp_password_local = ", "******")  #print("smtp_password_local = ", smtp_password_local)
print("send_to             = ", send_to)
print("send_from           = ", send_from)
print("subject             = ", subject)
print("body                = ", body)
print("send_cc             = ", send_cc)
print("smtp_port_local     = ", smtp_port_local)
print("attachment          = ", attachment)
print("")

msg = EmailMessage()
msg['Subject']        = subject
msg['From']           = send_from
msg['To']             = send_to

#add cc only if cc not empty 
if send_cc != "null":
    msg['Cc']         = send_cc    

msg.set_content(body)


# add attachment if required 
if (attachment == "yes"):
    try:
        dirname = os.path.dirname(__file__)                                          # present dir
        file = os.path.join(dirname, "../format_101/temp/HPK_format_101.txt") # up 1 dir and then further
        
        # hieronder test
        #file = "C:/NetBeansProjects/turbowin_jws/dist/jlink/turbowin_jws/bin/logs/format_101/temp/HPK_format_101.txt"
        # test
        
        # NB see: https://docs.python.org/3/library/email.examples.html
        with open(file, 'rb') as fp:
        #    data = fp.read()
            
            #msg.add_attachment(fp.read(), maintype=maintype, subtype=subtype, filename=filename)
            msg.add_attachment(fp.read(), maintype='application', subtype='octet-stream', filename=('iso-8859-1', '', 'HPK_format_101.txt')) 
            
                
        #msg.add_attachment(data, maintype='application', subtype='octet-stream', filename=('iso-8859-1', '', 'HPK_format_101.txt'))
        #msg.add_header('Content-Disposition', 'attachment', filename=('iso-8859-1', '', 'HPK_format_101.txt'))  #set filename

    except:
        print("Unable to open attachment ../format_101/temp/HPK_format_101.txt") # always ths path
        sys.exit(14)




# -----  TESTING BEGIN  -----
#
#smtp_mode      ="SMTP_HOST_SHIP" # "GMAIL_SSL" #"YAHOO_TLS" #"LOCALHOST_SHIP"  #"YAHOO_SSL" # "YAHOO_TLS"   # "SMTP_HOST_SHIP"
#
# ------ TESTING END ------
 


# Send the message via local SMTP server.
# https://docs.python.org/3/library/smtplib.html
#
# NB For normal use, you should only require the initialization/connect, sendmail(), and SMTP.quit() methods. An example is included below.
#
try:
#    with smtplib.SMTP_SSL(smtp_host, smtp_port, timeout = 60) as smtp:  #fom python version 3.3   # time out necessary??????
#        #smtp.ehlo(smtp_host)
#        #smtp.starttls()
#        #smtp.connect()
#        smtp.login(smtp_user_name, smtp_password)
#        smtp.send_message(msg)
#        # smtp.quit()                                 # NB due to the with construction is quit in principle not necessary


#    smtp = smtplib.SMTP_SSL(smtp_host, smtp_port, timeout = 60)  #   # time out necessary??????
#        #smtp.ehlo(smtp_host)
#        #smtp.starttls()
#        #smtp.connect()
#    smtp.login(smtp_user_name, smtp_password)
#    smtp.send_message(msg)
#    smtp.quit()                                 # NB due to the with construction is quit in principle not necessary


    # Create a secure SSL context
    context = ssl.create_default_context()


    if (smtp_mode == "GMAIL_TLS") :

#        send_to        = 'martin.stam@home.nl'
#        send_from      = 'turbowin.observations@gmail.com'
#        subject        = 'dit is een test via ' + smtp_mode
#        body           = "This is an obs test body line"
        
#        msg = EmailMessage()
#        msg['Subject'] = subject
#        msg['From']    = send_from
#        msg['To']      = send_to
#        msg.set_content(body)
        
        smtp_host      = smtp_host_local               # 'smtp.gmail.com'
        smtp_port      = 587                           # moet komen na msg = EmailMessage() !!!!!!!!!????????
        smtp_password  = smtp_password_local           #
         
        smtp_server = smtplib.SMTP(smtp_host, smtp_port)
        smtp_server.starttls(context=context)          # Secure the connection

        smtp_server.login(send_from, smtp_password)
        smtp_server.send_message(msg)
        smtp_server.quit()
        
    elif (smtp_mode == "GMAIL_SSL"):
#        send_to        = 'martin.stam@home.nl'
#        send_from      = 'turbowin.observations@gmail.com'
#        subject        = 'dit is een test via ' + smtp_mode
#        body           = "This is an obs test body line"
        
#        msg = EmailMessage()
#        msg['Subject'] = subject
#        msg['From']    = send_from
#        msg['To']      = send_to
#        msg.set_content(body)
        
        smtp_host      = smtp_host_local                 # 'smtp.gmail.com'
        smtp_port      = 465                             # moet komen na msg = EmailMessage() !!!!!!!!!????????
        smtp_password  = smtp_password_local             #

        smtp_server = smtplib.SMTP_SSL(smtp_host, smtp_port)
        smtp_server.login(send_from, smtp_password)
        smtp_server.send_message(msg)
        smtp_server.quit()
    
    elif (smtp_mode == "YAHOO_TLS"):
#        send_to        = 'martin.stam@home.nl'
#        send_from      = 'turbowin@yahoo.com'
#        subject        = 'dit is een test via ' + smtp_mode
#        body           = "This is an obs test body line"
        
#        msg = EmailMessage()
#        msg['Subject'] = subject
#        msg['From']    = send_from
#        msg['To']      = send_to
#        msg.set_content(body)
        
        smtp_host      = smtp_host_local               #'smtp.mail.yahoo.com'
        smtp_port      = 587                           # moet komen na msg = EmailMessage() !!!!!!!!!????????
        smtp_password  = smtp_password_local           #

        smtp_server = smtplib.SMTP(smtp_host, smtp_port)
        smtp_server.starttls(context=context) # Secure the connection

        smtp_server.login(send_from, smtp_password)
        smtp_server.send_message(msg)
        smtp_server.quit()
 
    elif (smtp_mode == "YAHOO_SSL"):
#        send_to        = 'martin.stam@home.nl'
#        #smtp_port      = 465
#        send_from      = 'turbowin@yahoo.com'
#        subject        = 'dit is een test via ' + smtp_mode
#        body           = "This is an obs test body line"
        
#        msg = EmailMessage()
#        msg['Subject'] = subject
#        msg['From']    = send_from
#        msg['To']      = send_to
#        msg.set_content(body)
        
        smtp_host      = smtp_host_local                    # 'smtp.mail.yahoo.com'
        smtp_port      = 465                                # moet komen na msg = EmailMessage() !!!!!!!!!????????
        smtp_password  = smtp_password_local                #
        
        smtp_server   = smtplib.SMTP_SSL(smtp_host, smtp_port)
        smtp_server.login(send_from, smtp_password)
        smtp_server.send_message(msg)
        smtp_server.quit()
        
    elif (smtp_mode == "SMTP_HOST_SHIP"):
# -------- TESTING BEGIN -------
#        smtp_host_local       = "smtp.ziggo.nl"
#        smtp_password_local   = "xxxxxx" 
#        smtp_port_local       = 587
#
#        send_to        = 'martin.stam@home.nl'
#        send_from      = 'martin.stam@home.nl'               #'martin.stam@knmi.nl'              # NB must be a valid email adress in the KNMI domain? (test: lex.snijders@knmi.nl -> ok)
#        subject        = 'dit is een test via ' + smtp_mode + ' YES password'                     # 'YES password'
#        body           = "This is an obs test body line"
#        attachment     = "yes"
#       
#        msg = EmailMessage()
#        msg['Subject'] = subject
#        msg['From']    = send_from
#        msg['To']      = send_to
#        msg.set_content(body)
#        
#        dirname = os.path.dirname(__file__) 
#        print("dirname =  ", dirname);
#        file = os.path.join(dirname, "../python/format_101/temp/HPK_format_101.txt")
#        print("file = ", file)
#        
#        if (attachment == "yes"):
#            try:
#                dirname = os.path.dirname(__file__)                                       # present dir
#                file = os.path.join(dirname, "../python/format_101/temp/HPK_format_101.txt") # up 1 dir and then further
#        
#                with open(file, 'rb') as fp:
#                    data = fp.read()
#                
#                msg.add_attachment(data, maintype='application', subtype='octet-stream')
#
#            except:
#                print("Unable to open attachment ../python/format_101/temp/HPK_format_101.txt")
#                sys.exit(14)
#        
#        
# ---------- TESTING END -----------        
 
        if (smtp_password_local != "null"):
  
            smtp_host      = smtp_host_local                  # 'smtp.ziggo.nl'
            smtp_port      = int(smtp_port_local)             # moet komen na msg = EmailMessage() !!!!!!!!!????????
            #smtp_port      = 587
            smtp_password  = smtp_password_local              # 
         
            smtp_server = smtplib.SMTP(smtp_host, smtp_port)
            smtp_server.starttls(context=context) # Secure the connection

            smtp_server.login(send_from, smtp_password)
            smtp_server.send_message(msg)
            smtp_server.quit()
  
        else:
            # password and port will not be used
            smtp_host      = smtp_host_local                   #'smtp.ziggo.nl' 
            smtp_server = smtplib.SMTP(smtp_host)
            smtp_server.send_message(msg)
            smtp_server.quit()
        
    else:   
        print ("invalid smtp_mode (",smtp_mode,")")
        sys.exit(2) 


#except (IOError, OSError) as ex:
#    print("error sending email to " + send_to + ".", "Error: " +  str(ex))
#    #self.retry((to_name, to_addr, subject, text, html), countdown=cfg['MAIL_RETRY'])
    
except smtplib.SMTPServerDisconnected:
    print("This exception is raised when the server unexpectedly disconnects, or when an attempt is made to use the SMTP instance before connecting it to a server.")
    sys.exit(3)
except smtplib.SMTPSenderRefused:
    print("Sender address refused. In addition to the attributes set by on all SMTPResponseException exceptions, this sets ‘sender’ to the string that the SMTP server refused.")
    sys.exit(4)
except smtplib.SMTPRecipientsRefused:
    print("All recipient addresses refused. The errors for each recipient are accessible through the attribute recipients, which is a dictionary of exactly the same sort as SMTP.sendmail() returns.")
    sys.exit(5)
except smtplib.SMTPDataError:
    print(" The SMTP server refused to accept the message data.")
    sys.exit(6)
except smtplib.SMTPConnectError:
    print("Error occurred during establishment of a connection with the server.")
    sys.exit(7)
except smtplib.SMTPHeloError:
    print("The server refused our HELO message.")
    sys.exit(8)
except smtplib.SMTPNotSupportedError:
    print("The command or option attempted is not supported by the server.")
    sys.exit(9)
except smtplib.SMTPAuthenticationError:
    print("SMTP authentication went wrong. Most probably the server didn’t accept the username/password combination provided.")    
    sys.exit(10)   
except smtplib.SMTPResponseException:
    print("Base class for all exceptions that include an SMTP error code. These exceptions are generated in some instances when the SMTP server returns an error code. The error code is stored in the smtp_code attribute of the error, and the smtp_error attribute is set to the error message.")
    sys.exit(11)   
except smtplib.SMTPException:
    print("Subclass of OSError that is the base exception class for all the other exceptions provided by this module.")
    sys.exit(12) 
except:
    # And finally, if you're still getting an SMTPAuthenticationError with an error code of 534, then you'll need to do yet another step for this to work.
    
    print ("not specified error; server reachable?; your email adress valid?; mail port (",smtp_port,") open?")
    sys.exit(13)
# NB reserved: print("Unable to open attachment ../python/format_101/temp/HPK_format_101.txt",)     
# NB reserved: sys.exit(14)   
else:
    print('mail sent successfully to: ', send_to) 
    sys.exit(0)



