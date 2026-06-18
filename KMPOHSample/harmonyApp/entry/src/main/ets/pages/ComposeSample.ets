import { registerComposeInteropBuilder } from 'compose/src/main/ets/compose/ArkUIView';
import {
  buttonBuilder,
  textBuilder,
  labelBuilder,
  textInputBuilder,
  buttonWrapContentBuilder,
  layerBuilder,
  verticalListBuilder,
  horizontalListBuilder,
} from './ComposeInterops';

export function registerComposeInteropBuilders() {
  registerComposeInteropBuilder('button', buttonBuilder)
  registerComposeInteropBuilder('label', labelBuilder)
  registerComposeInteropBuilder('buttonWrapContent', buttonWrapContentBuilder)
  registerComposeInteropBuilder('textInput', textInputBuilder)
  registerComposeInteropBuilder('layer', layerBuilder)
  registerComposeInteropBuilder('text', textBuilder)
  registerComposeInteropBuilder('verticalList', verticalListBuilder)
  registerComposeInteropBuilder('horizontalList', horizontalListBuilder)
}
