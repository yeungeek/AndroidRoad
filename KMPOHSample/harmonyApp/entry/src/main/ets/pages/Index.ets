import { ArkUIViewController, Compose } from 'compose';
import { hilog } from '@kit.PerformanceAnalysisKit';
import nativeApi from 'libentry.so';

const DOMAIN = 0x0000;

@Entry
@Component
struct Index {
  private controller: ArkUIViewController | undefined = undefined;

  @State errorMessage: string = 'Native module not ready';

  aboutToAppear() {
    if (nativeApi === undefined) {
      hilog.error(DOMAIN, 'Compose', 'nativeApi is undefined, cannot create controller');
      this.errorMessage = 'nativeApi is undefined';
      return;
    }
    try {
      this.controller = nativeApi.MainArkUIViewController();
      const state = this.controller ? 'created' : 'not_created';
      hilog.info(DOMAIN, 'Compose', 'controller init state: %{public}s', state);
      if (!this.controller) {
        this.errorMessage = 'Controller creation failed (returned null)';
      }
    } catch (e) {
      hilog.error(DOMAIN, 'Compose', 'Exception creating controller: %{public}s', JSON.stringify(e));
      this.errorMessage = 'Exception creating controller: ' + JSON.stringify(e);
    }
  }

  build() {
    Column() {
      if (this.controller) {
        Compose({
          controller: this.controller,
          libraryName: 'entry',
          onBackPressed: () => this.controller!.onBackPress()
        })
      } else {
        Text(this.errorMessage)
      }
    }
    .width('100%')
    .height('100%')
  }


  onPageHide() {
    if (this.controller) {
      this.controller.onPageHide();
    }
  }

  onPageShow() {
    if (this.controller) {
      this.controller.onPageShow();
    }
  }

  onBackPressed(): boolean {
    return this.controller ? this.controller.onBackPress() : false;
  }
}
