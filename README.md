# Defining functional relation between the Ui and the AppState properties

This article talks about defining a functional relation between the App's state and it's UI.

### Relating App state and UI
The UI on the screen is the visual/graphical representation of the `AppState`, where the `AppState` consists of different variables.  For eg: If you consider a Car, it has a state with speed being one of its variables (property). The speed displayed on the Dashboard(Ui) is a visual representation of the car's state(Speed).
```
AppState = f(var1, var2.....)
Ui = f(AppState)

DashboardState = f(speed, isLightOn, seatBeltsTucked .....)
DashboardUi = f(DashboardState)
```


## Overview of state-based architectures ## 

![](https://cdn-images-1.medium.com/max/1600/1*z4usAajJ9xU5wXrwXVZFiA.png)

Modern reactive architectures like Redux, MVVM, and MVI have a focus on the state,  where the Ui observes the state changes and re-renders itself.

### Working
 * An action/event changes the AppState. 
 * The state changes are observed by the Ui and it re-renders itself. 
 * This keeps the Ui consistent with the state of the system.
 
### Concerns
* Suppose that a screen has a __large UI__ consisting of a number of views. An AppState change affecting a small part of the Ui would eventually end up invalidating the whole Ui.
* If the AppState changes frequently, managing the Ui becomes difficult which causes inconsistencies and the performance is also affected.

__Example:__ A stock market system where only the price changes very often and it occupies only a part of the screen.


## Solution
* The core problem is the entire Ui of the screen responding to every small or big AppState changes. 
* A better approach is to break the Ui into smaller Uis. Consequently these Uis will get updated only when the relevant properties of the AppState changes.

```Ui_view_1 = f(AppState.var_1, AppState.var_2) ```

### Implementation

For demonstration purpose, let's consider a Shopping cart which has the following Ui and AppState.

![](https://cdn-images-1.medium.com/max/1600/1*DMdX47CfzuyZgXJ_ANOD6w.png)

### Refactoring the earlier state architecture

The Ui is broken into multiple smaller Uis. Instead of the whole Ui depending on a single AppState, the smaller Uis now depend on the variables in the AppState. The new design looks like this 👇🏻.

![](https://cdn-images-1.medium.com/max/1600/1*lwKtG37jI_DtMHpmUqh7SA.png)

### 1. Convert the variables in the AppState to Flow.

Previously AppState was observed. But now, the state variables have to be converted to a Flow so that the smaller Uis can observe their changes.
```swift
data class CartState(
    val cartItems: MutableStateFlow<List<Item>>,
    val deliveryCharge: MutableStateFlow<Double>,
    val tax: MutableStateFlow<Double>
)
```

### 2. Creating UiModel

Each of the smaller Ui will have UiModel associated with it that contains the data necessary for rendering it. This acts as a bridge between the UI and the AppState. The advantage that we get is the Ui and UiModel pair can be used as reusable components throughout the app.

```swift
data class TotalAmountUiModel(
    val displayText: String,
    val amount: Double
)

data class CartItemUiModel(
    val name: String,
    val cost: String,
    val quantity: String
)
```

### 3. Introducing UiState
This class is responsible for encapsulating all the UiModels that were created above. Note that all the UiModels are defined as Flow as these UiModels need to be observed by the Ui.
```swift
data class UiState(
    val cartItemsUiModel: MutableStateFlow<List<CartItemUiModel>>,
    val totalAmountUiModel: MutableStateFlow<TotalAmountUiModel>,
    ....
    ....
)
```


### Intermediate — ViewModel/Presentation layer changes

This is the data holder class for the Activity/Fragment. __This is the crucial part where we define the dependent/functional relationship between the UiModels in UiState and the Variables in AppState__. Changes in the AppState variables result in the dependent UiModels being updated.

```swift
class CartPageViewModel() : ViewModel() {

    val appState: CartState = CartState()
    val uiState: UiState = UiState()

    val mapper = Mapper()

    fun onResume() {
        defineUiRelations()
    }

    private fun defineUiRelations() {

        // 1. Define the relation for [CartItemsUiModel]
        with(appState) {
            cartItems
                .map { 
                  // NOTE: UiModel is created using the AppState variable
                  cartItems -> mapper.toCartItemsUiModel(cartItems)
                }
                .onEach { cartItemsUiModel -> uiState.cartItemsUiModel.value = cartItemsUiModel }
                .launchIn(viewModelScope)
        }

        // 2. Define the relation for [TotalAmountUiModel]
        with(appState) {
            combine(cartItems, deliveryCharge, tax) { items: List<Item>, delivery: Double, tax: Double ->
                // NOTE: UiModel is created using the AppState variables
                mapper.toTotalUiModel(items, delivery, tax)
            }
            .onEach { totalAmount -> uiState.totalAmountUiModel.value = totalAmount }
            .launchIn(viewModelScope)

        }
    }

}

class Mapper {

    /**
     * Read as [TotalAmountUiModel] is dependent on list of [Item], [DeliveryAmount], and [Tax]
     */
    fun toTotalUiModel(items: List<Item>, delivery: Double, tax: Double): TotalAmountUiModel {
        return TotalAmountUiModel(
            displayText = "Total: ",
            amount = items.map { it.cost * it.quantity }.sumByDouble { it } + delivery + tax)
    }
}
```

#### Points to note
* `OnEach` is an operator for collecting the data from the Flows.
* `launchIn` defines the coroutine scope to collect the flow.
* `map` is an operator for listening to changes in a single Flow.
* `combine` is an operator for listening to the changes in multiple Flows.
* `Mapper` is a utility class that contains the mapping for the UiModels.


1. CartItemsUiModel is dependent on AppState.CartItems variable
2. TotalAmountUiModel is dependent on AppState.CartItems + AppState.DeliveryCharge + AppState.Tax

### 4. Lastly, Updating the Uis in the Activity

In the Activity, the change in the UiModels is observed by the respective Uis.

```swift
class CartPageActivity : AppCompatActivity() {

    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      
      with(vm.uiState) {
            // 1. Observe for the changes in cart items
            cartItemsUiModel
                .onEach { items
                    rv_cart_items.refresh(items)
                }
                .launchIn(lifecycleScope)
            
            // 2. Observe for the total amount changes
            totalAmountUiModel
                .onEach { uiModel -> 
                    tv_total_amount.text = "${uiModel.displayText} ${uiModel.amount}" 
                }.
                launchIn(lifecycleScope)
        }
      
    }
}
```

The smaller Uis updated themselves only when their associated UiModel changes. Also, the UiModels only change when their dependent variables in the AppState change. This helps in removing the unnecessary updating of the whole screen even for a small Ui change.


`AppState changes` → `Only relevant UiModels are updated` → `Consequently relevant Uis are updated`.


## Additionally defining state dependencies.

We want the `DeliveryCharge` and `Tax` to be set to zero when all the cart items are removed. So we define a unidirectional relation between the AppState variables. In this case, `DeliveryCharge` and `Tax` depend on `CartItems`

```swift
class  CartPageViewModel: ViewModel(){

  private fun onResume() {
   ...
   defineStateDependencies()
  }

  fun defineStateDependencies() {
    with(appSate) {

      cartItems
        .onEach { 
            if (it.isEmpty()) {
              deliveryCharge.value = 0.0
              tax.value = 0.0
            } else {
              deliveryCharge.value = // Get actual value
              tax.value =  // Get value
            }
          }
          .launchIn(viewModelScope)
        }
        
  }
}
```

Be extra careful when defining state dependencies. It’s not a recommended practice as you might end with cyclical dependencies. Make sure to use it sparingly.




