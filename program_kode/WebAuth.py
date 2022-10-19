from flask import Flask, flash, render_template, request, session, url_for, redirect
import os.path
import json

# Use bcrypt for password handling
import bcrypt
import re 

FORMAT = 'utf-8'
PASSWORDFILE = 'passwords'
PASSWORDFILEDELIMITER = ":"

app = Flask(__name__)
# The secret key here is required to maintain sessions in flask
app.secret_key = b'8852475abf1dcc3c2769f54d0ad64a8b7d9c3a8aa8f35ac4eb7454473a5e454c'

# Initialize Database file if not exists. Make it ready for json objects.
if not os.path.exists(PASSWORDFILE):
    with open(PASSWORDFILE, 'w') as f:
        users = []
        json.dump(users, f, indent = 4)
        f.close()

# Saves a users username and a hashed password to json file.
def save_user(user_cretentials):
    with open(PASSWORDFILE, "r") as file:
        data = json.load(file)
        file.close()
    
    with open(PASSWORDFILE, 'w') as file:
        data.append(user_cretentials)
        json.dump(data, file, indent = 4)
        file.close()

# Generating the password hash from the users password, create a json object, 
# send it to save_user()
def save_password(username, password):
    salt = bcrypt.gensalt()
    pw_hash = bcrypt.hashpw(password.encode(FORMAT), salt)
    json_pw = pw_hash.decode(FORMAT)
    user_credentials = {'username': username, 'password': json_pw}
    save_user(user_credentials)
    return    

# Check if the user already exists
def check_username(username):
    check = False
    with open(PASSWORDFILE, "r") as file:
        data = json.load(file)
        file.close()

    for element in data:
        if username == element['username']:
            check = True
        else:
            check = False

    return check
 
# Check if the given password on login is correct and authentication is successfull.
def check_password(username, password):
    with open(PASSWORDFILE, "r") as file:
        data = json.load(file)
        file.close()
    
    for element in data:
        if username == element['username']:
            json_pw = element['password']
            pw_hash = json_pw.encode(FORMAT)
            if bcrypt.checkpw(password.encode(FORMAT), pw_hash):
                return True
            else:
                check = False
        else:
            check = False

    return check
 
# Verify that the passwords strength is great.
def verify_password_strength(password):
    check = True
    if len(password) < 8:
        check = False
    
    count = 0
    digit_error = re.search(r"\d", password) is None
    uppercase_error = re.search(r"[A-Z]", password) is None
    lowercase_error = re.search(r"[a-z]", password) is None
    symbol_error = re.search(r"[ !#$%&'()*+,-./[\\\]^_`{|}~"+r'"]', password) is None
    if not digit_error:
        count += 1
    if not uppercase_error:
        count += 1
    if not lowercase_error:
        count += 1
    if not symbol_error:
        count += 1
 
    if count < 3:
        check = False

    return check 

@app.route('/')
def home():
    
    # Check if user is authenticated. If the user is authenticated he should se a 
    # different page than one user not logged in.
    if "user" in session:
        user = session['user']
        return render_template('loggedin.html', username = user)
    else:
        return render_template('home.html')


# Display register form
@app.route('/register', methods=['GET'])
def register_get():
    
    # Check if user is authenticated. If the user is authenticated he should se a 
    # different page than one user not logged in.
    if "user" in session:
        user = session['user']
        return render_template('loggedin.html', username = user)
    else:
        return render_template('register.html')


# Handle registration data
@app.route('/register', methods=['POST'])
def register_post():
    
    # Gets user input for registration. Check if user already exists. 
    # Check if password is written correct. Check if the password is strong enough.
    # If everythin is ok the user is added to password file and redirected to login.
    username = request.form['username'] 
    password1 = request.form['password']
    password2 = request.form['matchpassword']
    if check_username(username):
        message = "Username already exists. Try a different username."
        return render_template('register.html', error = message)
    
    if password1 != password2:
        message = "Passwords dont match! Try again."
        return render_template('register.html', error = message)

    if not verify_password_strength(password1):
        message = "Password must contain 8 or more characters. It must contain 3 of the following: uppercase, lowercase, digits or symbols."
        return render_template('register.html', error = message)

    save_password(username, password1)

    return redirect(url_for('login_get'))
    

# Display login form
@app.route('/login', methods=['GET'])
def login_get():
    
    # Check if user is authenticated. If the user is authenticated he should se a 
    # different page than one user not logged in.
    if "user" in session:
        user = session['user']
        return render_template('loggedin.html', username = user)
    else:
        return render_template('login.html')


# Handle login credentials
@app.route('/login', methods=['POST'])
def login_post():

    # Check if user exists, if not then user redirected to register.html
    # Check if password is correct, if it is the user gets added to session
    username = request.form['username'] 
    password = request.form['password'] 
    
    if not check_username(username):
        message = "User does not exists. Please register."
        flash(message)
        return redirect(url_for('register_get'))
        #render_template('register.html', error = message)
    
    if not check_password(username, password):
        message = "Wrong password! Try again."
        return render_template('login.html', error = message)
    else:
        session["user"] = username
        return redirect(url_for('home'))


@app.route('/logout')
def logout():
    
    # Remove user from session when user push the logout button.
    session.pop("user", None)
    return redirect(url_for('login_get'))


if __name__ == '__main__':

    # TODO: Add TSL
    app.run(debug=True)
    
