<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isErrorPage="true"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>   
<!DOCTYPE html>
<html lang="en">

<head>
    <style>
        * {
            padding:0;
            margin:0;
        }
        .error {
            height: 100vh;
            text-align: center;
            background: #afafaf;
            overflow: hidden;
            display: flex;
            align-items: center;
            color: #FFF;
            font-family: "Varela Round", Sans-serif;
            text-shadow: 0 30px 10px rgba(0, 0, 0, 0.15);
            flex-wrap: wrap;
            justify-content: center;
            flex-direction: column;
        }

        .error img {
            max-width: 200px;
            margin-bottom: 20px;
            display: inline-block;
        }

        .error p {
            font-size: 18px;
            margin-top: 0;
        }

        .error h3 {
            font-size: 115px;
            margin-left: 0;
            display: Block;
            margin-right: 0;
            margin-top: 0;
            margin-bottom: 40px;
        }

        .error a {
            color: inherit;
            text-decoration: none;
            font-size: 20px;
            margin-top: 30px;
            display: block;
        }

    </style>
</head>

<body>
    <div class="error">
        <div class="img">
            <img src="${pageContext.request.contextPath}/resource/images/error.png" alt="">
        </div>
        <h3>500</h3>
        <p>Sever Error</p>
        <a href="/main.do">Go home</a>
    </div>
</body>
</html>