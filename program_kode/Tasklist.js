'use strict'

const requestGet = document.querySelector('.GETbtn');
const requestPost = document.querySelector('.POSTbtn');
const requestPut = document.querySelector('.PUTbtn');
const requestDone = document.querySelector('.DONEbtn');
const requestDelete = document.querySelector('.DELETEbtn');
const requestClear = document.querySelector('.CLEARbtn');

var Url = 'http://localhost:5000/api/items/';

const li = (text) => {
    const element = document.createElement('li');
    element.innerHTML = text;
    return element.outerHTML;
}

const displayOnScreen = (outputLine) =>{
    const task_list = document.getElementById('display');
    task_list.innerHTML = outputLine;
}

const sendHttpRequest = (method, urlPath, data) => {
    const promise = new Promise((resolve, reject) => {
        const req = new XMLHttpRequest();
        req.open(method, urlPath);
        req.responseType = 'json';
        if (data){
            req.setRequestHeader('Content-Type', 'application/json');
        }
        
        req.onload = () => { 
            if(req.status >= 400){
                reject(req.response);    
            }
            resolve(req.response);
        };

        req.onerror = () => {
            reject("Bad nettwork connection!")
        }
        req.send(JSON.stringify(data));
    });
    return promise;
    
};

const getData = (check) => {
    if (check !== 1){
        document.querySelector('.Feedback').textContent = "";
    }
    const labelID = document.getElementById('taskID');
    const labelInput =document.getElementById('newTask');
    const labelText = labelID.value;
    labelID.value = '';
    labelInput.value ='';
     
    var taskId = parseInt(labelText);
    var check = 0;
    if (labelText !== '' && Number.isInteger(taskId) === true){
        var newUrl = Url + labelText;
        check = 1;
    }else{
        var newUrl = Url;
    }
    sendHttpRequest('GET', newUrl).then(responseData => {
        const taskList = [];
        if (check === 0){
            responseData.items.forEach(element => {
                var compl3te = "";
                if(element.done === false){
                    compl3te = "Not check"
                }else{
                    compl3te = "_Check__"
                }
                const textLine = 'Status: ' + compl3te + '.Task ID: ' + element.id + '.      Task: ' + element.name;
                const onScreen = li(textLine);
                taskList.push(onScreen);               
            });
        }else{
            const textLine = 'Status: ' + responseData.item.done + '. Task ID: ' + responseData.item.id + '.      Task: ' + responseData.item.name;
            const onScreen = li(textLine);
            taskList.push(onScreen);
        }
        displayOnScreen(taskList.join(''));
    })
    .catch(err => {
        
        document.querySelector('.Feedback').textContent = JSON.stringify(err);
        console.log(element);
        
    }); 
};

const postData = () => {
    document.querySelector('.Feedback').textContent = "";
    const labelID = document.getElementById('taskID');
    const labelInput = document.getElementById('newTask');
    const labelText = labelInput.value;
    labelID.value = '';
    labelInput.value = '';
    var newTask;
    
    if (labelText !== ''){
        newTask = labelText;
    }else{
        document.querySelector('.Feedback').textContent = "Please type a task, you dont need to input ID";
        return;
    }
    var jsonObj = {"name": newTask}
        
    sendHttpRequest('POST', Url, jsonObj)
    .catch(err => {
        
        document.querySelector('.Feedback').textContent = JSON.stringify(err);
        console.log(element);
        
    }); 
    setTimeout(getData(1), 500);  
};

const putData = () => {
    document.querySelector('.Feedback').textContent = "";
    const labelID = document.getElementById('taskID');
    const itemID = labelID.value;
    const labelInput = document.getElementById('newTask');
    const labelText = labelInput.value;
    labelID.value = '';
    labelInput.value = '';
    var taskId = parseInt(itemID);
    var newTask;
    
    if (itemID !== '' && Number.isInteger(taskId) === true){
        var newUrl = Url + itemID;
    }else{
        var newUrl = Url;
    }
    
    if (labelText !== ''){
        newTask = labelText;
    }else{
        document.querySelector('.Feedback').textContent = "Please input a task-ID and the new task for this ID";
        return;
    }
    var jsonObj = {"name": newTask}
    sendHttpRequest('PUT', newUrl, jsonObj)
    .catch(err => {    
        document.querySelector('.Feedback').textContent = JSON.stringify(err);
        console.log(element);    
    }); 
    setTimeout(getData(1), 500);
};

const doneData = () => {
    document.querySelector('.Feedback').textContent = "";
    const labelID = document.getElementById('taskID');
    const itemID = labelID.value;
    const labelInput =document.getElementById('newTask');
    const labelText = labelInput.value;
    labelID.value = '';
    labelInput.value ='';
    var taskId = parseInt(itemID);
    if (itemID !== '' && Number.isInteger(taskId) === true){
        var newUrl = Url + itemID;
        
    }else{
        document.querySelector('.Feedback').textContent = "Please input a task-ID, on the message you want to check or uncheck.";
        return;
    }
    
    sendHttpRequest('GET', newUrl).then(responseData => {
        if(responseData.item.done === false){
            var jsonObj = {"done": true};
        }else{
            var jsonObj = {"done": false};
        }
        sendHttpRequest('PUT', newUrl, jsonObj);   
    })
    .catch(err => {
        
        document.querySelector('.Feedback').textContent = JSON.stringify(err);
        console.log(element);
        
    }); 
    setTimeout(getData(1), 500);
};

const deleteData = () => {
    document.querySelector('.Feedback').textContent = "";
    const labelID = document.getElementById('taskID');
    const itemID = labelID.value;
    const labelInput =document.getElementById('newTask');
    const labelText = labelInput.value;
    labelID.value = '';
    labelInput.value ='';
    var taskId = parseInt(itemID);
    if (itemID !== '' && Number.isInteger(taskId) === true){
        var newUrl = Url + itemID;
    }else{
        document.querySelector('.Feedback').textContent = "Please input the ID on the task you want to remove.";
        return;
    }
    sendHttpRequest('DELETE', newUrl)
    .catch(err => {
        
        document.querySelector('.Feedback').textContent = JSON.stringify(err);
        console.log(element);
        
    });        
    setTimeout(getData(1), 500);
};

const clearData = () => {
    document.querySelector('.Feedback').textContent = "";
    const labelID = document.getElementById('taskID');
    const labelInput =document.getElementById('newTask');
    labelID.value = '';
    labelInput.value ='';
    
    sendHttpRequest('GET', Url).then(responseData => {
        var newUrl;
        var jsonObj = {"done": false};
        if (responseData.items.length === 0){
            document.querySelector('.Feedback').textContent = "No tasks in the list.";
            return;
        }else{
            for (var i = 1; i <= responseData.items.length; i++){
                newUrl = Url + i;
                sendHttpRequest('PUT', newUrl, jsonObj);
                document.querySelector('.Feedback').textContent = "All tasks uncheked";
            }
        }           
    })
    .catch(err => {
        
        document.querySelector('.Feedback').textContent = JSON.stringify(err);
        console.log(element);
        
    }); 
    setTimeout(getData(1), 1000);
};

requestGet.addEventListener('click', getData);
requestPost.addEventListener('click', postData);
requestPut.addEventListener('click', putData);
requestDone.addEventListener('click', doneData);
requestDelete.addEventListener('click', deleteData);
requestClear.addEventListener('click', clearData);

getData();
