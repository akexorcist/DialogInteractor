[ ![Download](https://api.bintray.com/packages/akexorcist/maven/dialog-interactor/images/download.svg?version=1.0.0) ](https://bintray.com/akexorcist/maven/dialog-interactor/1.0.0/link)

Dialog Interactor
Interactor between dialog and activity or fragment that support lifecycle awareness by Android Architecture Components's LiveData

## Introduction
When you have to create some dialog and send the result of dialog to the Activity/Fragment with listener .

When problem of this regular dialog is about listener will be lost when user rotate the screen (do anything that made configuration changes occur) and your Activity or Fragment won't get any event from dialog anymore because listener does not have configuration changes support. 

![Dialog Interactor](https://raw.githubusercontent.com/akexorcist/Android-DialogInteractor/master/image/dialog_interactor-001.jpg)

To solve this problem you have to use LiveData from Android Architecture Components instead of simple listener. But it's so hard to use it. We have write a lot of code to replace listener with LiveData. 

And that is the reason why I built this library.

## Download
Maven
```
<dependency>
  <groupId>com.akexorcist</groupId>
  <artifactId>dialog-interactor</artifactId>
  <version>1.0.0</version>
</dependency>
```

Gradle
```
compile 'com.akexorcist:dialog-interactor:1.0.0'
```

## Feature
* Event listener with configuration changes supported
* Powered by ViewModel and LiveData (Android Architecture Components)

## How does it works?
![Dialog Interactor](https://raw.githubusercontent.com/akexorcist/Android-DialogInteractor/master/image/dialog_interactor-002.jpg)

Activity/Fragment call the dialog directly like regular dialog but event listener from dialog will be converted to LiveData by Mapper. This LiveData will be used by ViewModel. Activity/Fragment that observe the ViewModel will retrieve the data. But before that, Mapper will convert the LiveData back to listener that made the code in Activity/Fragment easier to handle than LiveData

## Usage
To build your own dialog with this library, you have to create 4 classes as following
* *Listener* of dialog that extends from `DialogListener`
* *Mapper* class that extends from `EventMapper`
* *ViewModel* for the Activity/Fragment who observe the dialog's event listener
* *Dialog* class that extends from `InteractorDialog`


### Listener
Event listener functions for dialog

Add the functions in listener as you want with `key: String?` and `data: Bundle?`. They are required for the library.

```kotlin
interface EditDialogListener: DialogListener {
    fun onEditSuccess(name: String?, key: String?, data: Bundle?)

    fun onEditFailure(error: String?, key: String?, data: Bundle?)
```

`key`  Key of dialog for identify the event. Very necessary when call same dialog with different situations.

For example
```kotlin
// Create dialog
const val KEY_EDIT_NAME = "key_edit_name"
const val KEY_EDIT_ADDRESS = "key_edit_address"
const val KEY_EDIT_BIRTH_DATE = "key_edit_birth_date"
EditDialog.Builder()
    .setKey(KEY_EDIT_NAME)
    .build()
    .show(...)

// Dialog event listener
val listener = Object : EditDialogListener {
    fun onEditSuccess(name: String?, key: String?, data: Bundle?) {
        when (key) {
            KEY_EDIT_NAME -> {
                // User edited the name
            }
            KEY_EDIT_ADDRESS -> ...
            KEY_EDIT_BIRTH_DATE -> ...
        } 
    }
    ...
}
```

`data` Bundle class that you can put any data inside it and retrieve it in dialog's event listener. This will help you to reduce the data declaration in the class that you have to store it and wait until the process in the dialog done then call it to do next things.

For example
```kotlin
// Create dialog
val userId = "89307"
EditDialog.Builder()
    .setData(Bundle().apply {
        putString("user_id", userId")
    }
    .build()
    .show(...)

// Dialog event listener
val listener = Object : EditDialogListener {
    fun onEditSuccess(name: String?, key: String?, data: Bundle?) {
        val userId = data?.getString("user_id")
        // userId = "89307"
    }
    ...
}
```

### Mapper
Event listener - LiveData converter 
```kotlin
class EditDialogMapper : EventMapper<EditDialogListener>() {
    companion object {
        const val EVENT_SUCCESS = "event_success"
        const val EVENT_FAILURE = "event_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): EditDialogListener =
            object : EditDialogListener {
        override fun onEditSuccess(name: String?, key: String?, data: Bundle?) {
            event.value = DialogEvent.Builder(
                event = EVENT_SUCCESS, 
                key = key, 
                data = data?.apply {
                    putString(EditDialog.EXTRA_EDITED_NAME, name)
                }
            ).build()
        } 

        override fun onEditFailure(error: String?, key: String?, data: Bundle?) {
            event.value = DialogEvent.Builder(
                event = EVENT_FAILURE, 
                key = key, 
                data = data?.apply {
                    putString(EditDialog.EXTRA_ERROR_MESSAGE, error)
                }
            ).build()
        } 
    }

    override fun toListener(event: DialogEvent, listener: EditDialogListener) {
        when (event.getEvent()) {
            EVENT_SUCCESS -> listener.onEditSuccess(
                name = event.getData()?.getString(EditDialog.EXTRA_EDITED_NAME),
                key = event.getKey(), 
                data = event.getData()
            )
            EVENT_FAILURE -> listener.onEditFailure(
                error = event.getData()?.getString(EditDialog.EXTRA_ERROR_MESSAGE), 
                key = event.getKey(), 
                data = event.getData()
        }
    }
}
```

* `toEvent(...)` Convert event listener to LiveData's event
* `toListener(...)` Convert LiveData's event to event listener

![Dialog Interactor](https://raw.githubusercontent.com/akexorcist/Android-DialogInteractor/master/image/dialog_interactor-003.jpg)

In `toEvent(...)` , create then DialogEvent for LiveData's event. 

```kotlin
val event = DialogEvent.Builder(
    event = EVENT_SUCCESS,
    key = key,
    data = data
).build()
```

`event` The kind of event listener
`key` The key of event that defined when create the dialog
`data` The bundle data when create the dialog. You can put more data inside this bundle when dialog have to send any result to Activity/Fragment.

```kotlin
val event = DialogEvent.Builder(
    ...
    data = data?.apply {
        putString(EditDialog.EXTRA_EDITED_NAME, error)
        // More data 
    }
).build()
```

In `toListener(...)` , use `getEvent()` to check what kind of event listener is. Then call the event listener and all data that function needed

```kotlin
when (event.getEvent()) {
    EVENT_SUCCESS -> listener.onEditSuccess(
        name = event.getData()?.getString(EditDialog.EXTRA_EDITED_NAME), 
        key = event.getKey(), 
        data = event.getData()
    )
    EVENT_FAILURE -> ...
}
```

As you can see, `onEditSuccess` required `name` that I already put in the the bundle in  `toEvent(...)`  function. So I can get the value from the bundle.

### ViewModel
Prepare the dialog with `DialogLauncher` that you have to define the Mapper class of the dialog here
```kotlin
class DialogViewModel : ViewModel() {
    val edit = DialogLauncher(EditDialogMapper::class.java)
    // More dialog
}
```

### Dialog
Create the dialog by extends from `InteractorDialog` and define the Mapper, Listener and ViewModel as generic type 

```kotlin
class EditDialog : InteractorDialog<EditDialogMapper, EditDialogListener, DialogViewModel>() {
    override fun bindViewModel() = DialogViewModel::class.java
    override fun bindLauncher(viewModel: DialogViewModel) = viewModel.edit
}
```

`InteractorDialog` has 2 abstract methods. `bindViewModel()`  for binding the ViewModel class and `bindLauncher(...)` for binding the DialogLauncher in ViewModel

I do not talk too much about the dialog builder because you are already familiar with the dialog before. But `key` and `data` are required in dialog builder also.

```kotlin
class EditDialog : InteractorDialog<EditDialogMapper, EditDialogListener, DialogViewModel>() {
	...

	companion object {
		const val EXTRA_NAME = "extra_name"

		fun newInstance(name: String?, key: String?, data: Bundle?) = EditDialog().apply {
	            arguments = createBundle(key, data).apply {
	                putString(EXTRA_NAME, name)
	            }
	        }
	 }

	...

    class Builder {
        private var name: String? = null
        private var key: String? = null
        private var data: Bundle? = null

        fun setName(name: String?): Builder = this.apply {
            this.name = name
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): EditDialog = EditDialog.newInstance(name, key, data)
    }
}
```

Just pass the `key` and `data` with `createBundle(...)` the extension method then put any data in the same bundle as you want. You don't have to handle the `key` and `data` for configuration changes. `InteractorDialog` will handle it automatically.

Then implement your dialog code as you want.

To send the event listener from the dialog. Call `getListener()`  then call the event listener method from that it. And use `getKey()` for `key` and `getData()` for  and `data` 

```kotlin
class EditDialog : InteractorDialog<EditDialogMapper, EditDialogListener, DialogViewModel>() {
    fun onConfirmButtonClick() {
        val name = editTextName.text.toString()
        getListener().onEditSuccess(name, getKey(), getData())
    }
}
```

For more information about using the library. See the example code.

## Licence
Copyright 2019 Akexorcist
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:
 [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) 
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

