Sequence Diagram:-

[UI Layer]

   |
   | — DashboardViewModel.init() → loadDashboard()
   |

   
[UseCase]

   |
   | — invoke()
   |     → repository.fetchProfile()
   |     → repository.fetchTransactions(profile.id)
   |     → repository.fetchConfig()
   |

   
[Repository]

   |
   | — Returns Data OR throws exception
   |     → apiService.fetchProfile()
   |     → apiService.fetchTransactions(profile.id)
   |     → apiService.fetchConfig()
   |

   
[ViewModel]

   |
   | — Updates StateFlow: Loading → Success/Error
   |

   
[UI Layer]

   |
   | — Observes state → Compose renders Loading/Success/Error


To simulate errors in API, replace with below-
ApiModule -> provideApiService() -> return FakeApiService(FakeApiService.FailPoint.TRANSACTIONS)
