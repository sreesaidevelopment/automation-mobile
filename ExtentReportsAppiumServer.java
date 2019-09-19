	ExtentReports extent;
	ExtentTest logger;
	
	
	@BeforeTest
	 public void startReport(){
	 extent = new ExtentReports (System.getProperty("user.dir") +"/ExtentReport.html", true);
	 extent.addSystemInfo("Host Name", "SoftwareTestingMaterial")
	       .addSystemInfo("Environment", "Automation Testing")
	       .addSystemInfo("User Name", "Sreesai");
	  extent.loadConfig(new File("/Users/mtpl/Automation/olx/extent-config.xml"));
	 }
	
	 @AfterTest
	 public void getResult(ITestResult result){ 
	 if(result.getStatus() == ITestResult.FAILURE){
	 logger.log(LogStatus.FAIL, "Test Case Failed is "+result.getName());
	 logger.log(LogStatus.FAIL, "Test Case Failed is "+result.getThrowable());
	 }else if(result.getStatus() == ITestResult.SKIP){
	 logger.log(LogStatus.SKIP, "Test Case Skipped is "+result.getName());
	 }
	 extent.endTest(logger);
	 }
	 
	
	public void startAppiumServer() {
		//Set Capabilities
		cap = new DesiredCapabilities();
		//Build the Appium service
		builder = new AppiumServiceBuilder();
		builder.withIPAddress(serverIp);
		builder.usingPort(Port);
		builder.withCapabilities(cap);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
		
		AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
		service.start();
		System.out.println("Appium server started");
	}
	
	
	public void setupServer() {
		if(!checkIfServerIsRunnning(Port)) {
			startAppiumServer();
		} else {
			System.out.println("Appium Server already running on Port - " + Port);
		}
	}
	
	public void stopServer() {
		service.stop();
		System.out.println("Server stopped");
	}


	public boolean checkIfServerIsRunnning(int port) {
			
			boolean isServerRunning = false;
			ServerSocket serverSocket;
			try {
				serverSocket = new ServerSocket(port);
				serverSocket.close();
			} catch (IOException e) {
				//If control comes here, then it means that the port is in use
				isServerRunning = true;
			} finally {
				serverSocket = null;
			}
			return isServerRunning;
		}

		if (verifyWelcomeScreenIsDisplaying()) {
				userLoginFromWelcomeScreen(Mobile, Password);
			}
			if (loggedInUserMobile.equalsIgnoreCase(Mobile)) {
				firstTimeLogin(noOfPhotos, Category, Location, Title, Description, Price, ContactPerson, Email);
			} else {
				logout();
				userLoginFromHomeScreen(Mobile, Password);
				firstTimeLogin(noOfPhotos, Category, Location, Title, Description, Price, ContactPerson, Email);
			}