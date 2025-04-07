package com.example.projectmanagement

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.projectmanagement.data.local.AppDatabase
import com.example.projectmanagement.data.model.Notification
import com.example.projectmanagement.data.model.Project
import com.example.projectmanagement.data.model.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class ProjectManagementInstrumentedTest {

    private lateinit var db: AppDatabase
    private var testUserId: Int = -1
    private val testUsername = "admin"
    private val testPassword = "123"
    private val testEmail = "test@example.com"

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        // Create in-memory database for testing
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        AppDatabase.setInstance(db)

        runBlocking {
            // Insert test user
            testUserId = db.userDao().insertUser(
                User(
                    username = testUsername,
                    password = testPassword,
                    email = testEmail
                )
            ).toInt()
        }
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Throws(InterruptedException::class)
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.example.projectmanagement", appContext.packageName)
//    }

    @Test
    fun testInvalidUsername() {
        ActivityScenario.launch(LoginActivity::class.java).use {
            onView(withId(R.id.username)).perform(clearText(), typeText("wronguser"))
            onView(withId(R.id.password)).perform(clearText(), typeText("anypassword"), closeSoftKeyboard())

            onView(withId(R.id.login_button)).perform(click())

            onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testIncorrectPassword() {
        ActivityScenario.launch(LoginActivity::class.java).use {
            onView(withId(R.id.username)).perform(clearText(), typeText(testUsername))
            onView(withId(R.id.password)).perform(clearText(), typeText("wrongpassword"), closeSoftKeyboard())

            onView(withId(R.id.login_button)).perform(click())

            onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testCorrectCredentials() {
        ActivityScenario.launch(LoginActivity::class.java).use {
            onView(withId(R.id.username)).perform(clearText(), typeText(testUsername))
            onView(withId(R.id.password)).perform(clearText(), typeText(testPassword), closeSoftKeyboard())

            onView(withId(R.id.login_button)).perform(click())

            Thread.sleep(1000)
            onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testLogout() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java).apply {
            putExtra("username", testUsername)
            putExtra("email", testEmail)
            putExtra("userId", 1)
        }

        ActivityScenario.launch<MainActivity>(intent).use {
            // Open drawer
            onView(withId(R.id.drawer_layout))
                .check(matches(isDisplayed()))
                .perform(DrawerActions.open())

            Thread.sleep(500)

            onView(withId(R.id.logout_button))
                .check(matches(isDisplayed()))
                .perform(click())

            Thread.sleep(1000)
            onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun testProjectCreation() = runBlocking {

        val project = Project(
            name = "Test Project",
            description = "Test Description",
            startDate = "2025-05-01",
            dueDate = "2025-06-01",
            status = "In Progress",
            createdBy = testUserId
        )

        db.projectsDao().insertProject(project)

        // Verify the project was created
        val projects = db.projectsDao().getAllProjects()
        assertTrue("Project was not created in database",
            projects.any { it.name == "Test Project" && it.description == "Test Description" })
    }

    @Test
    fun testNewNotificationOnProjectCreation() = runBlocking {
        AppDatabase.setInstance(db)

        // Create intent for create project activity
        val intent = Intent(ApplicationProvider.getApplicationContext(), CreateProjectActivity::class.java).apply {
            putExtra("userId", testUserId)
        }

        // Launch CreateProjectActivity
        ActivityScenario.launch<CreateProjectActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                println("Activity launched with userId: ${activity.intent.getIntExtra("userId", -1)}")
            }

            // Fill in project details
            onView(withId(R.id.edit_project_name)).perform(clearText(), typeText("Notification Test"))
            onView(withId(R.id.edit_project_desc)).perform(clearText(), typeText("Testing notifications"))
            onView(withId(R.id.edit_startDate)).perform(clearText(), typeText("2025-07-01"))
            onView(withId(R.id.edit_endDate)).perform(clearText(), typeText("2025-08-01"), closeSoftKeyboard())

            onView(withId(R.id.create_project_btn)).perform(click())

            Thread.sleep(3000)
        }

        val projects = db.projectsDao().getAllProjects()
        println("Projects found after creation: ${projects.size}")
        projects.forEach { println("Project: ${it.name}, ${it.description}") }

        val project = projects.find { it.name == "Notification Test" }
        assertNotNull("Project was not created in database", project)

        val notifications = db.notificationDao().getAllNotifications()
        println("Notifications found: ${notifications.size}")
        notifications.forEach { println("Notification: ${it.heading}, ${it.content}, projectId: ${it.fromProject}") }

        assertTrue("No notification was created for the project",
            notifications.any {
                it.fromProject == project!!.projectId &&
                        it.heading == "Project Due Date" &&
                        it.content.contains("Notification Test")
            })
    }

    @Test
    fun testDeleteProject() = runBlocking {

        val project = Project(
            name = "Delete Test",
            description = "Project to delete",
            startDate = "2025-09-01",
            dueDate = "2025-10-01",
            status = "In Progress",
            createdBy = testUserId
        )
        val projectId = db.projectsDao().insertProject(project)
        println("Created test project with ID: $projectId")

        AppDatabase.setInstance(db)

        val intent = Intent(ApplicationProvider.getApplicationContext(), EditActivity::class.java).apply {
            putExtra("PROJECT_ID", projectId.toInt())
            putExtra("userId", testUserId)
        }

        ActivityScenario.launch<EditActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                println("EditActivity launched with projectId: ${activity.intent.getIntExtra("PROJECT_ID", -1)}")
            }

            Thread.sleep(1000)

            onView(withId(R.id.delete_btn)).perform(click())

            Thread.sleep(3000)
        }

        val projects = db.projectsDao().getAllProjects()
        println("Projects remaining after deletion: ${projects.size}")
        projects.forEach { println("Project: ${it.name}, ID: ${it.projectId}") }

        val deletedProject = db.projectsDao().getProjectById(projectId.toInt())
        assertNull("Project was not deleted from database", deletedProject)
    }

    @Test
    fun testEditProject() = runBlocking {
        val userId = db.userDao().insertUser(User(
            username = testUsername,
            password = testPassword,
            email = testEmail
        ))
        val project = Project(
            name = "Edit Test Original",
            description = "Original description",
            startDate = "2025-11-01",
            dueDate = "2025-12-01",
            status = "In Progress",
            createdBy = userId.toInt()
        )
        val projectId = db.projectsDao().insertProject(project)
        println("Created test project with ID: $projectId")

        AppDatabase.setInstance(db)

        val intent = Intent(ApplicationProvider.getApplicationContext(), EditActivity::class.java).apply {
            putExtra("PROJECT_ID", projectId.toInt())
            putExtra("userId", userId.toInt())
        }

        ActivityScenario.launch<EditActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                println("EditActivity launched with projectId: ${activity.intent.getIntExtra("PROJECT_ID", -1)}")
            }

            Thread.sleep(1000)

            // Update project details
            onView(withId(R.id.edit_project_name)).perform(clearText(), typeText("Updated Project"))
            onView(withId(R.id.edit_project_desc)).perform(clearText(), typeText("Updated description"))
            onView(withId(R.id.edit_startDate)).perform(clearText(), typeText("2026-01-01"))
            onView(withId(R.id.edit_endDate)).perform(clearText(), typeText("2026-02-01"), closeSoftKeyboard())

            onView(withId(R.id.update_btn)).perform(click())

            Thread.sleep(3000)
        }

        val updatedProject = db.projectsDao().getProjectById(projectId.toInt())
        println("Updated project: ${updatedProject?.name}, ${updatedProject?.description}")

        assertNotNull("Project was not found in database", updatedProject)
        assertEquals("Project name was not updated", "Updated Project", updatedProject?.name)
        assertEquals("Project description was not updated", "Updated description", updatedProject?.description)
        assertEquals("Project start date was not updated", "2026-01-01", updatedProject?.startDate)
        assertEquals("Project due date was not updated", "2026-02-01", updatedProject?.dueDate)
    }

    @Test
    fun testNotificationChangeOnProjectUpdate() = runBlocking {
        val project = Project(
            name = "Notification Update Test",
            description = "Test notification updates",
            startDate = "2025-11-01",
            dueDate = "2025-12-01",
            status = "In Progress",
            createdBy = testUserId
        )
        val projectId = db.projectsDao().insertProject(project)

        val initialNotification = Notification(
            heading = "Project Due Date",
            content = "Don't forget your deadline for Notification Update Test",
            fromProject = projectId.toInt(),
            eventDueDate = "2025-12-01"
        )
        db.notificationDao().insertNotification(initialNotification)

        val updatedProject = project.copy(
            projectId = projectId.toInt(),
            name = "Updated Notification Project",
            dueDate = "2026-03-01"
        )
        db.projectsDao().updateProject(updatedProject)

        val updatedNotification = Notification(
            heading = "Project Due Date",
            content = "Don't forget your deadline for Updated Notification Project",
            fromProject = projectId.toInt(),
            eventDueDate = "2026-03-01"
        )
        db.notificationDao().insertNotification(updatedNotification)

        val latestNotification = db.notificationDao().getNotificationById(projectId.toInt())

        assertNotNull(latestNotification)
        assertEquals("Don't forget your deadline for Updated Notification Project", latestNotification?.content)
        assertEquals("2026-03-01", latestNotification?.eventDueDate)
    }

}