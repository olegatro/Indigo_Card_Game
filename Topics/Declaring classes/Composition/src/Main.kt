class OperatingSystem(var name: String = "test")
class DualBoot(var primaryOs: OperatingSystem = OperatingSystem("test"), var secondaryOs: OperatingSystem = OperatingSystem("test2"))